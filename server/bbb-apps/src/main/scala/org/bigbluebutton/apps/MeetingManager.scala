package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging

class MeetingManager(val pubSub: ActorRef) extends Actor with ActorLogging {
  var meetings = new collection.immutable.HashMap[String, MeetingActor]
  
  def receive = {
    case createMeetingRequest: CreateMeetingRequest => handleCreateMeetingRequest(createMeetingRequest)
    case _ => None
  }
  
  def handleCreateMeetingRequest(msg: CreateMeetingRequest) = {
    
    val meetingRef = context.actorOf(MeetingActor.props(pubSub), "bar")
  }
}