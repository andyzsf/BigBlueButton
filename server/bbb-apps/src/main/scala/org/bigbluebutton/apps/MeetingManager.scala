package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import org.bigbluebutton.apps.models.MeetingSession
import org.bigbluebutton.apps.protocol.MeetingCreated
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol.Ok

class MeetingManager(val pubsub: ActorRef) extends Actor with ActorLogging {
  /**
   * Holds our currently running meetings.
   */
  var meetings = new collection.immutable.HashMap[String, ActorRef]
  
  def receive = {
    case createMeetingRequest: CreateMeetingRequest => 
           handleCreateMeetingRequest(createMeetingRequest)
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
  def getValidSession(externalMeetingId: String): String = { 
    externalMeetingId + "-" + System.currentTimeMillis()
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
    if (! meetingExist(internalMeetingId)) {
      log.info("Creating meeting [{}] : [{}]", externalMeetingId, name)
      
      val sessionId = getValidSession(internalMeetingId)      
      val session = MeetingSession(name, externalMeetingId, sessionId)
      
      val meetingRef = context.actorOf(MeetingActor.props(pubsub, session, mConfig), sessionId)
      meetings += (sessionId -> meetingRef)
      
      log.debug("Replying to create meeting request. [{}] : [{}]", externalMeetingId, name)
      
      /**
       * TODO: Send a reply that the meeting has been creted successfully.
       */
      sender ! CreateMeetingRequestReply(session, msg.payload)
//      sender ! Ok(util.Random.nextInt(10000))

      pubsub ! MeetingCreated(session, msg.payload)      
    }   else {
      log.info("Meeting [{}] : [{}] is already running.", externalMeetingId, name) 
      /**
       * TODO: Send a reply that a meeting with that id is already running.
       */
    }  
  } 
}