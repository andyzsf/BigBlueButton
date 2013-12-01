package org.bigbluebutton.apps.protocol

import spray.json.DefaultJsonProtocol
import spray.json.JsObject
import org.bigbluebutton.apps.models.UsersApp._
import org.bigbluebutton.apps.models.Role._
import spray.json.DeserializationException

object RegisterUserRequestProtocol extends DefaultJsonProtocol {
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