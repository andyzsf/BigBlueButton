package org.bigbluebutton.apps.users.protocol

import spray.json.{DefaultJsonProtocol, JsObject, JsValue, JsString, DeserializationException, JsonFormat}
import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.models.MeetingIdAndName
import org.bigbluebutton.apps.users.messages.Result


case class UserJoinRequestMessage(header: Header, payload: UserJoinRequestPayload) 
case class UserJoinRequestPayload(meeting: MeetingIdAndName, session: String, token: String)
case class UserJoinResponseMessage(header: Header, payload: UserJoinResponsePayload)
case class UserJoinResponsePayload(meeting: MeetingIdAndName, session: String, 
                                   result: Result, user: Option[UserFormat])

case class RegisterUserRequest(header: Header, payload: User)

case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
case class JoinUserReply(header: Header, payload: JoinUserResponse)  

case class PresenterPayload(presenter: UserIdAndName, assigned_by: UserIdAndName)

case class UserFormat(external_id: String, name: String, 
	            role: Role.RoleType, pin: Int, welcome_message: String,
	            logout_url: String, avatar_url: String)
	            
object UserMessagesProtocol extends DefaultJsonProtocol {
  import HeaderAndPayloadJsonSupport._

  implicit object RoleJsonFormat extends JsonFormat[Role.RoleType] {
	def write(obj: Role.RoleType): JsValue = JsString(obj.toString)
	
	def read(json: JsValue): Role.RoleType = json match {
	    case JsString(str) => Role.withName(str)
	    case _ => throw new DeserializationException("Enum string expected")
	}
  }

  implicit val webIdentityFormat = jsonFormat1(WebIdentity)
  implicit val callerIdFormat = jsonFormat2(CallerId)
  implicit val voiceIdentityFormat = jsonFormat5(VoiceIdentity)
	  
  implicit val userFormat = jsonFormat7(UserFormat)
  implicit val userIdAndNameFormat = jsonFormat2(UserIdAndName)
  implicit val presenterFormat = jsonFormat2(PresenterPayload)
  implicit val meetingIdAndNameFormat = jsonFormat2(MeetingIdAndName)
  implicit val userJoinRequestPayloadFormat = jsonFormat3(UserJoinRequestPayload)
  implicit val userJoinRequestMessageFormat = jsonFormat2(UserJoinRequestMessage)
  implicit val resultFormat = jsonFormat2(Result)
  implicit val userJoinResponsePayloadFormat = jsonFormat4(UserJoinResponsePayload)
  implicit val userJoinResponseMessageFormat = jsonFormat2(UserJoinResponseMessage)  
}

