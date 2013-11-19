package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import org.bigbluebutton.apps.models.MeetingSession

class MeetingManager(val pubSub: ActorRef) extends Actor with ActorLogging {
  var meetings = new collection.immutable.HashMap[String, ActorRef]
  
  def receive = {
    case createMeetingRequest: CreateMeetingRequest => 
           handleCreateMeetingRequest(createMeetingRequest)
    case _ => None
  }
  
  def meetingExist(internalMeetingId: String): Boolean = {
      (meetings.keys find {k => k.startsWith(internalMeetingId)}) != None
  }
  
  def getValidSession(externalMeetingId: String): String = { 
    externalMeetingId + "-" + System.currentTimeMillis()
  }
  
  def handleCreateMeetingRequest(msg: CreateMeetingRequest) = {
    val externalMeetingId = msg.payload.externalId
    val name = msg.payload.name
    
    log.debug("Received create meeting request for [{}] : [{}]", externalMeetingId, name)
    
    val internalMeetingId = Util.toInternalMeetingId(externalMeetingId)
    if (! meetingExist(internalMeetingId)) {
      log.info("Creating meeting [{}] : [{}]", externalMeetingId, name)
      
      val sessionId = getValidSession(internalMeetingId)      
      val session = MeetingSession(name, externalMeetingId, sessionId)
      
      val meetingRef = context.actorOf(MeetingActor.props(pubSub, session), name)
      meetings += (sessionId -> meetingRef)
      
      meetingRef forward msg
      
      sender ! msg
    }     
  }
}