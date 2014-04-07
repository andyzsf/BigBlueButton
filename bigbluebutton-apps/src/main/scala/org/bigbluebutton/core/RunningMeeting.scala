package org.bigbluebutton.apps

import akka.actor.ActorRef
import akka.actor.ActorContext
import org.bigbluebutton.core.MeetingActor
import org.bigbluebutton.core.api.MessageOutGateway

object RunningMeeting {  
  def apply(meetingID: String, meetingName: String, recorded: Boolean, 
	          voiceBridge: String, duration: Long, outGW: MessageOutGateway)
              (implicit context: ActorContext) = 
                new RunningMeeting(meetingID, meetingName, recorded, 
	          voiceBridge, duration, outGW)(context)
}

class RunningMeeting (val meetingID: String, meetingName: String, val recorded: Boolean, 
                   val voiceBridge: String, duration: Long, val outGW: MessageOutGateway)
              (implicit val context: ActorContext) {
  
  val actorRef = context.actorOf(MeetingActor.props(meetingID, meetingName, recorded, 
	          voiceBridge, duration, outGW), meetingID)
  
}