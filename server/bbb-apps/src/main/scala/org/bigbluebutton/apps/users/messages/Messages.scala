package org.bigbluebutton.apps.users.messages

import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.users.data.User
import org.bigbluebutton.apps.users.data.Presenter
import org.bigbluebutton.apps.users.data.JoinedUser
import org.bigbluebutton.apps.users.data.UserIdAndName
import org.bigbluebutton.apps.users.data.CallerId


trait MeetingMessage{def session: Session}  

/**
 * Response status
 */
case class Response(success:Boolean, message: String)

/**
 * Message from 3rd-party to register a user.
 */
case class RegisterUserRequest(session: Session, user: User) extends MeetingMessage

/**
 * Reply message for the register user request.
 */
case class RegisterUserResponse(session: Session, response: Response,
                                token: String, user: User)

/**
 * Message from 3rd-party for a user joining the meeting.
 */                                
case class UserJoinRequest(session: Session, token: String)

/**
 * Reply to the join user request.
 */
case class UserJoinResponse(session: Session, response: Response,
                              token: String, user: Option[JoinedUser])

/**
 * Broadcast message to interested parties that a user has joined the meeting.
 */
case class UserJoined(session: Session, token: String, user: JoinedUser)

/**
 * Message that a user is leaving the meeting.
 */
case class UserLeave(session: Session, userId: String)

/**
 * Broadcast message to interested parties that a user has left the meeting.
 */
case class UserLeft(session: Session, user: JoinedUser)

/**
 * Message toq uery for a list of users in a meeting.
 */
case class GetUsersRequest(session: Session, requesterId: String)

/**
 * Reply to the get user request.
 */
case class GetUsersResponse(session: Session, requesterId: String, users: Seq[JoinedUser])
  
/**
 * Message to assign a presenter.
 */
case class AssignPresenter(session: Session, presenter: Presenter)  

/**
 * Broadcast message to tell users that a new presenter has been assigned.
 */
case class BecomePresenter(session: Session, presenter: Presenter)

/**
 * Broadcast message to tell users to become viewer.
 */
case class BecomeViewer(session: Session)

/**
 * Message that a user raised hand.
 */
case class RaiseHand(session: Session, user: UserIdAndName)

/**
 * Broadcast message that a user has raised her hand.
 */
case class HandRaised(session: Session, user: UserIdAndName)

/**
 * Message that a user has lowered hand.
 */
case class LowerHand(session: Session, user: UserIdAndName, loweredBy: UserIdAndName)

/**
 * Broadcast that a user has lowered hand.
 */
case class HandLowered(session: Session, user: UserIdAndName, loweredBy: UserIdAndName)

/**
 * Message that informs a user has joined the voice conference.
 */
case class VoiceUserJoin(userId: String, voiceConfId: String, callerId: CallerId, 
                         muted: Boolean, locked: Boolean, talking: Boolean,
                         metadata: Map[String, String])

/**
 * Message to mute a user.
 */
case class MuteUser(user: UserIdAndName, mute: Boolean, mutedBy: UserIdAndName)

/**
 * Broadcast message that a user will be muted.
 */
case class MuteUserRequest(session: Session, user: UserIdAndName, mute: Boolean, mutedBy: UserIdAndName)

/**
 * Message to voice conference server to mute the user.
 */
case class UserMuteRequest(session: Session, user: UserIdAndName, mute: Boolean,
                           metadata: Map[String, String])
                           
/**
 * Message from voice conference server that the user has been muted.
 */
case class UserMuted(userId: String, muted: Boolean, metadata: Map[String, String])
