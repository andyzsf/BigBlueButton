package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props

object MeetingActor {
	def props(pubSub: ActorRef): Props = Props(classOf[MeetingActor], pubSub)
}

class MeetingActor (val pubSub: ActorRef) extends Actor with ActorLogging {

  def receive = {
    case _ => None
  }
}