package org.bigbluebutton.endpoint

import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.models.MeetingIdAndName
import org.bigbluebutton.apps.users.messages.Result
import org.bigbluebutton.apps.users.messages.UserJoinResponse

case class ResultFormat(success: Boolean, message: String)

case class UserJoinResponseMessage(header: Header, response: UserJoinResponse)
case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
case class JoinUserReply(header: Header, payload: JoinUserResponse)                    
case class UserFormat(id: String, external_id: String, name: String, 
	            role: Role.RoleType, pin: Int, welcome_message: String,
	            logout_url: String, avatar_url: String)
	            
case class UserJoinRequestPayload(meeting: MeetingIdAndName, 
                                  session: String, token: String)
case class UserLeavePayload(meeting: MeetingIdAndName, 
                            session: String, user: UserIdAndName)
case class GetUsersRequestPayload(meeting: MeetingIdAndName, 
                                  session: String, requester: UserIdAndName) 
case class AssignPresenterPayload(meeting: MeetingIdAndName, 
                                  session: String, presenter: UserIdAndName,
                                  assigned_by: UserIdAndName)                                  