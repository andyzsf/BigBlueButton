package org.bigbluebutton.apps.protocol

import spray.json.DefaultJsonProtocol
import spray.json.JsObject
import org.bigbluebutton.apps.models.UsersApp._
import spray.json.DeserializationException
import org.bigbluebutton.apps.models.Role
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue

object RegisterUserRequestProtocol extends DefaultJsonProtocol {
	implicit object RoleJsonFormat extends JsonFormat[Role.RoleType] {
	    def write(obj: Role.RoleType): JsValue = JsString(obj.toString)
	
	    def read(json: JsValue): Role.RoleType = json match {
	      case JsString(str) => Role.withName(str)
	      case _ => throw new DeserializationException("Enum string expected")
	    }
	  }
  implicit val userFormat = jsonFormat7(User)
}

trait UsersAppMessageHandler {
  import RegisterUserRequestProtocol._
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