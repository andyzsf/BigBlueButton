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
case class UserFormat(id: String, external_id: String, name: String, 
	            role: Role.RoleType, pin: Int, welcome_message: String,
	            logout_url: String, avatar_url: String)	        
case class Duration(length: Int, allow_extend: Boolean, max: Int)
case class VoiceConference(pin: Int, number: Int)
case class PhoneNumber(number: String, description: String)
case class MeetingDescriptor(name: String, external_id: String, record: Boolean,
                welcome_message: String, logout_url: String,
                avatar_url: String, max_users: Int, 
                duration: Duration, voice_conference: VoiceConference,
                phone_numbers: Seq[PhoneNumber],
                metadata: Map[String, String])
case class CreateMeetingRequestPayload(meeting_descriptor: MeetingDescriptor)

case class CreateMeetingResponsePayload(meeting: MeetingIdAndName,
                session: String, meeting_descriptor: MeetingDescriptor)

case class MeetingCreatedEventPayloadFormat(meeting: MeetingIdAndName,
                session: String, meeting_descriptor: MeetingDescriptor)
                
case class UserJoinResponseMessage(header: Header, response: UserJoinResponse)
case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
case class JoinUserReply(header: Header, payload: JoinUserResponse)  
	            
case class UserJoinRequestPayload(meeting: MeetingIdAndName, 
                                  session: String, token: String)
case class UserLeavePayload(meeting: MeetingIdAndName, 
                            session: String, user: UserIdAndName)
case class GetUsersRequestPayload(meeting: MeetingIdAndName, 
                                  session: String, requester: UserIdAndName) 
case class AssignPresenterPayload(meeting: MeetingIdAndName, 
                                  session: String, presenter: UserIdAndName,
                                  assigned_by: UserIdAndName)                                  