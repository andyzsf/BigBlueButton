package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import org.bigbluebutton.apps.protocol.RegisterUserRequest
import org.bigbluebutton.apps.models.Users
import org.bigbluebutton.apps.models.MeetingSession

object MeetingActor {
	def props(pubSub: ActorRef, session: MeetingSession): Props = Props(classOf[MeetingActor], pubSub, session)
}

class MeetingActor (val pubSub: ActorRef, val session: MeetingSession) 
      extends Actor with Users with ActorLogging  {

  def receive = {
    case createMeetingRequest: CreateMeetingRequest => 
                       handleCreateMeetingRequest(createMeetingRequest)
    case registerUserRequest: RegisterUserRequest =>
    case _ => None
  }
  
  def handleCreateMeetingRequest(msg: CreateMeetingRequest) = {
    
  }
}