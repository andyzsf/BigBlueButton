package org.bigbluebutton.apps

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorLogging
import org.bigbluebutton.apps.models.MeetingSession
import org.bigbluebutton.apps.protocol.MeetingCreated
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.protocol.UserMessages.RegisterUserRequest
import org.bigbluebutton.apps.protocol.Protocol._
import org.bigbluebutton.apps.Meeting.CreateMeetingResponse
import org.bigbluebutton.apps.protocol.MeetingMessages.{CreateMeetingRequest, CreateMeetingRequestMessage}

class MeetingManager(val pubsub: ActorRef) extends Actor with ActorLogging {
  /**
   * Holds our currently running meetings.
   */
  private var meetings = new collection.immutable.HashMap[String, Meeting]
  
  def receive = {
    case createMeetingRequest: CreateMeetingRequestMessage => 
           handleCreateMeetingRequest(createMeetingRequest)
    case registerUser : RegisterUserRequest =>
           handleRegisterUser(registerUser)
    case "test" => {sender ! "test"; pubsub ! "test"}
    case _ => None
  }
  
  /**
   * Returns true if a meeting with the passed id exist.
   */
  def meetingExist(internalMeetingId: String): Boolean = {
      (meetings.keys find {k => k.startsWith(internalMeetingId)}) != None
  }
  
  /**
   * Creates an internal id out of the external id. 
   */
  def getValidSession(internalId: String): String = { 
    internalId + "-" + System.currentTimeMillis()
  }
  
  def createMeeting(config: MeetingDescriptor, internalId: String):Meeting = {  
	val sessionId = getValidSession(internalId)
	val session = createSession(config.name, config.externalId, sessionId)
	val meetingRef = Meeting(session, pubsub, config)	      
	storeMeeting(session.session, meetingRef)
    meetingRef
  }
  
  def storeMeeting(session: String, meeting: Meeting) = {
    meetings += (session -> meeting) 
  }
  
  def getSessionFor(internalId: String):Option[String] = {
	 meetings.keys find {key => key.startsWith(internalId)}
  }
  
  def getMeeting(internalId: String):Option[Meeting] = {
    for {
      session <- getSessionFor(internalId)
      meeting <- meetings.get(session)
    } yield meeting
  }
  
  def createSession(name: String, externalId: String, sessionId: String):MeetingSession = {   
	 MeetingSession(name, externalId, sessionId)    
  }
  
  def getMeetingUsingSessionId(sessionId: String):Option[Meeting] = {
    for { meeting <- meetings.get(sessionId) } yield meeting    
  }
  
  def getSessionIdFromHeader(header: Header):Option[String] = {
    for { sessionId <- header.meeting.sessionId } yield sessionId
  }
  
  def handleRegisterUser(msg: RegisterUserRequest) = {
    val meeting = for {
      sessionId <- getSessionIdFromHeader(msg.header)
      meeting <- getMeetingUsingSessionId(sessionId)
    } yield meeting
    
    meeting.map {m => m.actorRef forward msg}
  }
  
  /**
   * Handle the CreateMeetingRequest message.
   */
  def handleCreateMeetingRequest(msg: CreateMeetingRequestMessage) = {
    val mConfig = msg.payload.meeting
    val externalMeetingId = mConfig.externalId
    val name = mConfig.name
    
    log.debug("Received create meeting request for [{}] : [{}]", externalMeetingId, name)
    
    val internalId = Util.toInternalMeetingId(externalMeetingId)
    
    getMeeting(internalId) match {
      case Some(meetingActor) => {
	      log.info("Meeting [{}] : [{}] is already running.", externalMeetingId, name) 
	      sender ! CreateMeetingResponse(false, msg.payload.meeting, Some("Meeting exists"), None)         
      }
      case None => {
	      log.info("Creating meeting [{}] : [{}]", externalMeetingId, name)
	      val meetingRef = createMeeting(mConfig, internalId)	      
	      	      
	      log.debug("Replying to create meeting request. [{}] : [{}]", externalMeetingId, name)
	      
	      sender ! CreateMeetingResponse(true, msg.payload.meeting, None,  Some(meetingRef.session))	
	      pubsub ! MeetingCreated(meetingRef.session, msg.payload.meeting)         
      }
    } 
  } 
}