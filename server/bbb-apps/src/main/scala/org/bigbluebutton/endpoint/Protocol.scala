package org.bigbluebutton.endpoint

import spray.json.{DefaultJsonProtocol, JsValue, JsString, DeserializationException, JsonFormat}
import spray.json.DefaultJsonProtocol._
import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.models.MeetingIdAndName
import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport.headerFormat


/** JSON Conversion Protocol **/	            
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
  implicit val userFormat = jsonFormat8(UserFormat)
  implicit val userIdAndNameFormat = jsonFormat2(UserIdAndName)
  implicit val meetingIdAndNameFormat = jsonFormat2(MeetingIdAndName)
  implicit val userJoinRequestPayloadFormat = jsonFormat3(UserJoinRequestPayload)
  implicit val userJoinRequestMessageFormat = jsonFormat2(UserJoinRequestMessage)
  implicit val resultFormat = jsonFormat2(ResultFormat)
  implicit val userJoinResponsePayloadFormat = jsonFormat4(UserJoinResponseJsonPayload)
  implicit val userJoinResponseJsonMessageFormat = jsonFormat2(UserJoinResponseJsonMessage)  
  implicit val userLeavePayloadFormat = jsonFormat3(UserLeavePayload)
  implicit val userLeaveMessageFormat = jsonFormat2(UserLeaveMessage)
  implicit val getUsersRequestPayloadFormat = jsonFormat3(GetUsersRequestPayload)
  implicit val getUsersRequestMessageFormat = jsonFormat2(GetUsersRequestMessage) 
  implicit val assignPresenterPayloadFormat = jsonFormat4(AssignPresenterPayload)
  implicit val assignPresenterMessageFormat = jsonFormat2(AssignPresenterMessage)
}

