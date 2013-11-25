package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import org.bigbluebutton.apps.models.MeetingSession
import org.bigbluebutton.apps.protocol.MeetingCreated
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol.Ok
import org.bigbluebutton.apps.models.MeetingConfig

class MeetingManager(val pubsub: ActorRef) extends Actor with ActorLogging {
  /**
   * Holds our currently running meetings.
   */
  private var meetings = new collection.immutable.HashMap[String, Meeting]
  
  def receive = {
    case createMeetingRequest: CreateMeetingRequest => 
           handleCreateMeetingRequest(createMeetingRequest)
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
  def getValidSession(internalMeetingId: String): String = { 
    internalMeetingId + "-" + System.currentTimeMillis()
  }
  
  def createMeeting(config: MeetingConfig, internalMeetingId: String):Meeting = {  
	val sessionId = getValidSession(internalMeetingId)
	val session = createSession(config.name, config.externalId, sessionId)
	val meetingRef = new Meeting(session, pubsub, config, context)	      
	storeMeeting(session.session, meetingRef)
    meetingRef
  }
  
  def storeMeeting(session: String, meeting: Meeting) = {
    meetings += (session -> meeting) 
  }
  
  def getSessionFor(internalId: String):Option[String] = {
	 meetings.keys find {key => key.startsWith(internalId)}
  }
  
  def getMeeting(internalMeetingId: String):Option[Meeting] = {
    for {
      session <- getSessionFor(internalMeetingId)
      meeting <- meetings.get(session)
    } yield meeting
  }
  
  def createSession(name: String, externalId: String, sessionId: String):MeetingSession = {   
	 MeetingSession(name, externalId, sessionId)    
  }
  
  /**
   * Handle the CreateMeetingRequest message.
   */
  def handleCreateMeetingRequest(msg: CreateMeetingRequest) = {
    val mConfig = msg.payload
    val externalMeetingId = mConfig.externalId
    val name = mConfig.name
    
    log.debug("Received create meeting request for [{}] : [{}]", externalMeetingId, name)
    
    val internalMeetingId = Util.toInternalMeetingId(externalMeetingId)
    
    getMeeting(internalMeetingId) match {
      case Some(meetingActor) => {
	      log.info("Meeting [{}] : [{}] is already running.", externalMeetingId, name) 
	      sender ! CreateMeetingRequestReply(false, "Meeting already exist.", meetingActor.session)         
      }
      case None => {
	      log.info("Creating meeting [{}] : [{}]", externalMeetingId, name)
	      val meetingRef = createMeeting(mConfig, internalMeetingId)	      
	      	      
	      log.debug("Replying to create meeting request. [{}] : [{}]", externalMeetingId, name)
	      
	      sender ! CreateMeetingRequestReply(true, "Meeting has been created.", meetingRef.session)	
	      pubsub ! MeetingCreated(meetingRef.session, msg.payload)         
      }
    } 
  } 
}