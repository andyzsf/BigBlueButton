package org.bigbluebutton.apps.protocol

import spray.json.JsValue
import org.bigbluebutton.apps.models.UsersApp.User
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport
import org.bigbluebutton.apps.models.UsersApp.JoinedUser

case class Header(event: HeaderEvent, meeting: HeaderMeeting)
case class HeaderEvent(name: String, timestamp: Long, 
                       source: String, reply: Option[ReplyHeader])
case class ReplyHeader(to: String, correlationId: String)                       
case class HeaderMeeting(name: String, externalId: String, sessionId: Option[String] = None)
case class HeaderAndPayload(header: Header, payload: JsValue)
case class ReplyStatus(status: String, message: String, error: Option[Int])

 
case class StatusCode(code: Int, message: String)
case class ErrorCode(code: Int, message: String)
case class Response(status: StatusCode, errors: Option[Seq[ErrorCode]] = None)
  
case class MessageProcessException(message: String) extends Exception(message)

object InMessageNameContants {
  val CreateMeetingRequestMessage = "CreateMeetingRequest"
  val RegisterUserRequestMessage = "RegisterUserRequest"
}

object HeaderAndPayloadJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {  
  implicit val replyHeaderFormat = jsonFormat2(ReplyHeader)
  implicit val headerEventFormat = jsonFormat4(HeaderEvent)
  implicit val headerMeetingFormat = jsonFormat3(HeaderMeeting)
  implicit val headerFormat = jsonFormat2(Header)  
  implicit val headerAndPayloadFormat = jsonFormat2(HeaderAndPayload)
}

object StatusCodes extends Enumeration {
  type StatusCodeType = Value
  
  val OK = Value(200, "OK")
  val NOT_MODIFIED = Value(304, "Not Modified")
  val BAD_REQUEST =  Value(400, "Bad Request")
  val UNAUTHORIZED = Value(401, "Unauthorized")
  val FORBIDDEN = Value(403, "Forbidden")
  val NOT_FOUND = Value(404, "Not Found")
  val NOT_ACCEPTABLE = Value(406, "Not Acceptable")
  val INTERNAL_SERVER_ERROR = Value(500, "Internal Server Error")
  val BAD_GATEWAY = Value(502, "Bad Gateway")
  val SERVICE_UNAVAILABLE = Value(503, "Service Unavailable")
}

object ErrorCodes extends Enumeration {
  type ErrorCodeType = Value
  
  val INVALID_TOKEN = Value(89, "Invalid or expired token")   
  
}