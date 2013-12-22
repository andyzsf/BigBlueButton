package org.bigbluebutton.apps.users.unmarshalling


import org.bigbluebutton.apps.users._
import spray.json.DeserializationException
import org.bigbluebutton.apps.models.Role
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsObject
import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.users.data.WebIdentity
import org.bigbluebutton.apps.users.data.VoiceIdentity
import org.bigbluebutton.apps.users.data.User
import org.bigbluebutton.apps.users.data.JoinedUser
import org.bigbluebutton.apps.users.data.CallerId
import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._
import UserMessagesProtocol.userFormat
import org.bigbluebutton.apps.protocol.InMessage


trait UsersAppMessageHandler {
  import UserMessagesProtocol._
/*  
  def handleRegisterUserRequest(header: Header, payload: JsObject):InMessage = {
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
  */
}