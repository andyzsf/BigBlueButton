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
  private var meetings = new collection.immutable.HashMap[String, ActorRef]
  
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
  
  def createMeeting(session: MeetingSession, config: MeetingConfig):ActorRef = {
    context.actorOf(MeetingActor.props(pubsub, session, config), session.session)
  }
  
  def storeMeeting(session: String, meetingRef: ActorRef) = {
    meetings += (session -> meetingRef) 
  }
  
  def getSessionFor(internalId: String):Option[String] = {
	 meetings.keys find {key => key.startsWith(internalId)}
  }
  
  def getMeeting(internalMeetingId: String):Option[ActorRef] = {
    getSessionFor(internalMeetingId) match {
      case Some(session) => meetings.get(session)
      case None => None
    }
  }
  
  def createSession(name: String, externalId: String, internalId: String):MeetingSession = {
	 val sessionId = getValidSession(internalId)      
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
	      /**
	       * TODO: Send a reply that a meeting with that id is already running.
	       */         
      }
      case None => {
	      log.info("Creating meeting [{}] : [{}]", externalMeetingId, name)
	      
	      val session = createSession(name, externalMeetingId, internalMeetingId)
	      val meetingRef = createMeeting(session, mConfig)	      
	      storeMeeting(session.session, meetingRef)
	      
	      log.debug("Replying to create meeting request. [{}] : [{}]", externalMeetingId, name)
	      
	      sender ! CreateMeetingRequestReply(true, "Meeting has been created.", session)	
	      pubsub ! MeetingCreated(session, msg.payload)         
      }
    } 
  } 
}