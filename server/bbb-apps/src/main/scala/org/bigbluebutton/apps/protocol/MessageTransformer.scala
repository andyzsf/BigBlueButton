package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.JsValue
import spray.json.DefaultJsonProtocol

object ExtractMessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val headerFormat = jsonFormat4(Header)
}

object MessageTransformer {
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
}