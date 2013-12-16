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
import org.bigbluebutton.apps.users.UsersAppHandler
import org.bigbluebutton.apps.users._
import org.bigbluebutton.apps.users.messages._


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
    case ujr: UserJoinRequest     => handleUserJoinRequest(ujr)
    case userLeave: UserLeave     => handleUserLeave(userLeave)
    case gur: GetUsersRequest     => handleGetUsersRequest(gur)
    case apm: AssignPresenter     => handleAssignPresenter(apm)
    case rhm: RaiseHand           => handleRaiseHand(rhm)
    case lhm: LowerHand           => handleLowerHand(lhm)
    case vuj: VoiceUserJoin       => handleVoiceUserJoin(vuj)
    case mum: MuteUser            => handleMuteUser(mum)
    case _ => None
  }
 

}