package org.bigbluebutton.apps.users

import org.bigbluebutton.apps.models.Session
import Model._



object Messages {
  trait MeetingMessage{def session: Session}  
  
  case class Response(success:Boolean, message: String)
  
  case class RegisterUserRequest(session: Session, user: User) extends MeetingMessage
  
  case class RegisterUserResponse(session: Session, response: Response,
                                  token: String, user: User)
  
  case class UserJoinRequest(session: Session, token: String)
  case class UserJoinResponse(session: Session, response: Response,
                              token: String, user: Option[JoinedUser])
  case class UserJoined(session: Session, token: String, user: JoinedUser)
  
  case class UserLeave(session: Session, userId: String)
  case class UserLeft(session: Session, user: JoinedUser)
  
  case class GetUsersRequest(session: Session, requesterId: String)
  case class GetUsersResponse(session: Session, requesterId: String, users: Seq[JoinedUser])
  
  case class ChangeUserStatus(meetingID: String, userID: String, status: String, value: Object) 
  case class AssignPresenter(meetingID: String, newPresenterID: String, newPresenterName: String, assignedBy: String)  
}