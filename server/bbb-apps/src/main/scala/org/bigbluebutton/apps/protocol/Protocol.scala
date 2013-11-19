package org.bigbluebutton.apps.protocol

import spray.json.JsValue

case class Header(name: String, timestamp: Long, correlation: String, source: String)
case class HeaderAndPayload(header: Header, payload: JsValue)

object InMessageNameContants {
  val CreateMeetingRequestMessage = "CreateMeetingRequest"
}


case class RegisterUserRequest(header: Header, payload: String)
case class AssignPresenter(header: Header, payload: String)