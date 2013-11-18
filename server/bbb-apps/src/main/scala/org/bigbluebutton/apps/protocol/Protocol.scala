package org.bigbluebutton.apps.protocol

import spray.json.JsValue
import org.bigbluebutton.apps.models.Core.MeetingSpec


case class Header(name: String, timestamp: Long, correlation: String, source: String)
case class HeaderAndPayload(header: Header, payload: JsValue)

case class CreateMeetingRequest(header: Header, payload: MeetingSpec)