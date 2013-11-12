package org.bigbluebutton.apps.protocol

import spray.json.DefaultJsonProtocol
import Message._
import spray.json.JsObject

import spray.json.JsonParser

object Message {
   case class Meeting(id: String, name: String, session: String)
   case class MessageHeader(name: String, timestamp: Long, meeting: Meeting)
}

object MessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val meetingFormat = jsonFormat3(Meeting)
  implicit val messageHeaderFormat = jsonFormat3(MessageHeader)
}

import Message._
import MessageHeaderJsonProtocol._

object MessageHandler {
  def extractMessageHeader(msg: JsObject):Option[MessageHeader] = {
    msg.fields.get("header") match {
      case Some(header) => {
        val h = header.convertTo[MessageHeader]
        Some(h)
      }
      case None => None
    }
  }
  
  def processMessage(msg: String) = {
    val jsonMsg = JsonParser(msg).asJsObject
    
    val header = extractMessageHeader(jsonMsg)
  }
} 