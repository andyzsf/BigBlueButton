package org.bigbluebutton.apps.users.protocol

import spray.json.{DefaultJsonProtocol, JsObject, JsValue, JsString, DeserializationException, JsonFormat}

import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.models.Session


case class RegisterUserRequest(header: Header, payload: User)
case class JoinUserRequest(header: Header, token: String) 
case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
case class JoinUserReply(header: Header, payload: JoinUserResponse)  

case class PresenterPayload(presenter: UserIdAndName, assigned_by: UserIdAndName)

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
	  
  implicit val userFormat = jsonFormat7(User)
  implicit val joinedUserFormat = jsonFormat6(JoinedUser)
	
  implicit val joinUserResponseFormat = jsonFormat3(JoinUserResponse)
  implicit val joinUserReplyFormat = jsonFormat2(JoinUserReply)
  implicit val userIdAndNameFormat = jsonFormat2(UserIdAndName)
  implicit val presenterFormat = jsonFormat2(PresenterPayload)

}

