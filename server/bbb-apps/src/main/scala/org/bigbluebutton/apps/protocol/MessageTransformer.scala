package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.JsValue
import spray.json.DefaultJsonProtocol
import spray.json.JsonParser
import org.parboiled.errors.ParsingException
import InMessageNameContants._

object ExtractMessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val headerFormat = jsonFormat4(Header)
}

trait MessageTransformer extends MeetingMessageHandler{
  import ExtractMessageHeaderJsonProtocol._
  
  def extractMessageHeader(msg: JsObject): Option[Header] = {
    msg.fields.get("header") match {
      case Some(header) => {
        val h = header.convertTo[Header]
        Some(h)
      }
      case None => None
    }
  }
 
  def extractPayload(msg: JsObject): Option[JsValue] = {
    msg.fields.get("payload")  
  }
  
  def jsonMessageToObject(msg: String): Option[JsObject] = {
    try {
      val msgObject = JsonParser(msg).asJsObject
      Some(msgObject)
    } catch {
      case e: ParsingException => None
    }
  }
    
  def transformMessage(jsonMsg: String):Option[InMessage] = {
    val jsonObj = jsonMessageToObject(jsonMsg)
    if (jsonObj != None) {
      val msgObj = jsonObj get
      val header = extractMessageHeader(msgObj)
      val payload = extractPayload(msgObj)
      
      if (header != None && payload != None) {
        processMessage(header get, (payload get).asJsObject)
      } else {
        None
      }
    } else {
      None
    }
  }
  
  def processMessage(header: Header, payload:JsObject):Option[InMessage] = {
    header.name match {
        case CreateMeetingRequestMessage  => {
          handleCreateMeetingRequest(header, payload)
        }
        case _ => None
    }
  }
}