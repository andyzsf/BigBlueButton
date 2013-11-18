package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import org.bigbluebutton.apps.protocol.RegisterUserRequest
import org.bigbluebutton.apps.models.Users

object MeetingActor {
	def props(pubSub: ActorRef): Props = Props(classOf[MeetingActor], pubSub)
}

class MeetingActor (val pubSub: ActorRef) extends Actor with Users with ActorLogging  {

  def receive = {
    case createMeetingRequest: CreateMeetingRequest =>
    case registerUserRequest: RegisterUserRequest =>
    case _ => None
  }
}