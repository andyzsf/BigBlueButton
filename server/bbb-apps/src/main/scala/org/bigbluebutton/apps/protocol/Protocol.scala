package org.bigbluebutton.apps.protocol

import spray.json.JsValue
import org.bigbluebutton.apps.models.MeetingSpec


case class Header(name: String, timestamp: Long, correlation: String, source: String)
case class HeaderAndPayload(header: Header, payload: JsValue)

object InMessages {
  val CreateMeetingRequestMessage = "CreateMeetingRequest"
  
}
case class CreateMeetingRequest(header: Header, payload: MeetingSpec)
case class RegisterUserRequest(header: Header, payload: String)
case class AssignPresenter(header: Header, payload: String)