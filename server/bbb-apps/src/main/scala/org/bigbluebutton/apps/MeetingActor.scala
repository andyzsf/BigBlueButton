package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import org.bigbluebutton.apps.models.UsersApp
import org.bigbluebutton.apps.models.MeetingSession
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol.MeetingCreated
import org.bigbluebutton.apps.models.MeetingConfig
import org.bigbluebutton.apps.models.UsersApp.RegisteredUser
import org.bigbluebutton.apps.models.UsersAppHandler
import org.bigbluebutton.apps.protocol.UserMessages.RegisterUserRequest

object MeetingActor {
	def props(pubsub: ActorRef, session: MeetingSession, 
	    meeting: MeetingConfig): Props = 
	      Props(classOf[MeetingActor], pubsub, session, meeting)
}

class MeetingActor (val pubsub: ActorRef, val session: MeetingSession, 
                    val meeting: MeetingConfig) extends Actor with ActorLogging
                    with UsersAppHandler {
  
  def receive = {                       
    case rur: RegisterUserRequest => handleRegisterUser(rur)
    
    case _ => None
  }
 

}