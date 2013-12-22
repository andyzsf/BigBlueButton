package org.bigbluebutton.apps.users.unmarshalling

import spray.json.DefaultJsonProtocol
import spray.json.JsObject
import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import spray.json.JsonFormat
import org.bigbluebutton.apps.models.Role
import spray.json.JsValue
import spray.json.JsString
import spray.json.DeserializationException


case class RegisterUserRequest(header: Header, payload: User)
case class JoinUserRequest(header: Header, token: String) 
case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
case class JoinUserReply(header: Header, payload: JoinUserResponse)  


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
}
