package org.bigbluebutton.apps.protocol

import spray.json.DefaultJsonProtocol
import spray.json.JsObject
import org.bigbluebutton.apps.models.UsersApp._
import spray.json.DeserializationException
import org.bigbluebutton.apps.models.Role
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue
import org.bigbluebutton.apps.models.UsersApp.{WebIdentity, CallerId, VoiceIdentity}

object UserMessages {
	case class RegisterUserRequest(header: Header, payload: User) extends InMessage
	case class JoinUserRequest(header: Header, token: String) extends InMessage
	case class JoinUserResponse(response: Response, token: String, joinedUser: Option[JoinedUser])
	case class JoinUserReply(header: Header, payload: JoinUserResponse)  
}

object UserMessagesProtocol extends DefaultJsonProtocol {
  import HeaderAndPayloadJsonSupport._
  import UserMessages._
  
	implicit object RoleJsonFormat extends JsonFormat[Role.RoleType] {
	    def write(obj: Role.RoleType): JsValue = JsString(obj.toString)
	
	    def read(json: JsValue): Role.RoleType = json match {
	      case JsString(str) => Role.withName(str)
	      case _ => throw new DeserializationException("Enum string expected")
	    }
	  }

	implicit val webIdentityFormat = jsonFormat1(WebIdentity)
	implicit val callerIdFormat = jsonFormat2(CallerId)
	implicit val voiceIdentityFormat = jsonFormat2(VoiceIdentity)
	  
	implicit val userFormat = jsonFormat7(User)
	implicit val joinedUserFormat = jsonFormat6(JoinedUser)
	
	implicit val joinUserResponseFormat = jsonFormat3(JoinUserResponse)
	implicit val joinUserReplyFormat = jsonFormat2(JoinUserReply)
}

trait UsersAppMessageHandler {
  import UserMessages._
  import UserMessagesProtocol._
  
  def handleRegisterUserRequest(header: Header, 
                                 payload: JsObject):InMessage = {
	payload.fields.get("user") match {
      case Some(user) => {
	      try {
	        val m = user.convertTo[User]
	        RegisterUserRequest(header, m)
	      } catch {
	        case e: DeserializationException => {
	          throw MessageProcessException("Failed to deserialize register user request message.")
	        }
	      }        
      }
      case None => throw MessageProcessException("Malformed register user request message.")
    }
  }
}