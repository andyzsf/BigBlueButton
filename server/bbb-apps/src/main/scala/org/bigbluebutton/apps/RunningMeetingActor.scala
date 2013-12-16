package org.bigbluebutton.apps

import akka.actor.Actor
import org.bigbluebutton.apps.protocol.MeetingMessages.CreateMeetingRequest
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import org.bigbluebutton.apps.users.UsersApp
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.users.Model.RegisteredUser
import org.bigbluebutton.apps.users.UsersAppHandler
import org.bigbluebutton.apps.users.Messages.{RegisterUserRequest, 
                     UserJoinRequest, UserLeave, GetUsersRequest}

object RunningMeetingActor {
	def props(pubsub: ActorRef, session: Session, 
	          meeting: MeetingDescriptor): Props = 
	      Props(classOf[RunningMeetingActor], pubsub, session, meeting)
}

class RunningMeetingActor (val pubsub: ActorRef, val session: Session, 
                    val meeting: MeetingDescriptor) extends Actor with ActorLogging
                    with UsersAppHandler {
  
  def receive = {                       
    case rur: RegisterUserRequest => handleRegisterUser(rur)
    case ujr: UserJoinRequest => handleUserJoinRequest(ujr)
    case userLeave: UserLeave => handleUserLeave(userLeave)
    case gur: GetUsersRequest => handleGetUsersRequest(gur)
    case _ => None
  }
 

}