package org.bigbluebutton.apps.protocol

import akka.actor.Actor
import spray.json.JsObject
import spray.json.JsValue
import spray.json.DefaultJsonProtocol
import akka.actor.ActorLogging

object HeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val timestampFormat = jsonFormat1(Timestamp)
  implicit val eventNameFormat = jsonFormat1(EventName)
  implicit val eventSourceFormat = jsonFormat1(EventSource)
  implicit val correlationFormat = jsonFormat1(Correlation)
  implicit val headerFormat = jsonFormat4(Header)
}

/**
 * MessageHandler receives messages from our endpoints.
 * It expects a JSON Object with the a header and payload fields.
 */
class MessageHandlerActor extends Actor with ActorLogging {
  import HeaderJsonProtocol._
  
  
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
  
  def receive = {
    case msgObj: JsObject => {
      val header = extractMessageHeader(msgObj)
      val payload = extractPayload(msgObj)
      
      if (header != None && payload != None) {
        //forwardMessage(header get, payload get)
      }
    }
    case headerAndPayload: HeaderAndPayload => println(headerAndPayload.toString)
    case unknownMessage => log.warning("Unable to handle message: [\n {} \n]", unknownMessage)
  }
}

