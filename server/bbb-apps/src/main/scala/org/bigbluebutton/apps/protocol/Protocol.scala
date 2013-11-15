package org.bigbluebutton.apps.protocol

import spray.json.JsValue

case class Timestamp(val timestamp: Long) extends AnyVal
case class EventName(val name: String) extends AnyVal
case class EventSource(val source: String) extends AnyVal
case class Correlation(val correlation: String) extends AnyVal

case class Header(name: String, timestamp: Long, correlation: String, source: String)
case class HeaderAndPayload(header: Header, payload: JsValue)