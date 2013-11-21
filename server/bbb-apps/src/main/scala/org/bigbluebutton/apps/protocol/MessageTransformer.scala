package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.JsValue
import spray.json.DefaultJsonProtocol
import spray.json.JsonParser
import org.parboiled.errors.ParsingException
import InMessageNameContants._
import akka.event.LoggingAdapter
import akka.event.slf4j.SLF4JLogging

object ExtractMessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val headerFormat = jsonFormat4(Header)
}

object MessageTransformer extends MeetingMessageHandler with SLF4JLogging {
  
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
    log.debug("Converting to json : {}", msg)
    
    try {
      val msgObject = JsonParser(msg).asJsObject
      Some(msgObject)
    } catch {
      case e: ParsingException => {
        log.error("Cannot parse message: {}", msg)
        None
      }
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
        println("Cannot header or payload from : " + jsonMsg)
        log.error("Cannot header or payload from : {}", jsonMsg)
        None
      }
    } else {
      println("Cannot convert json message: " + jsonMsg)
      log.error("Cannot convert json message: {}", jsonMsg)
      None
    }
  }
  
  def processMessage(header: Header, payload:JsObject):Option[InMessage] = {
    header.name match {
        case CreateMeetingRequestMessage  => {
          handleCreateMeetingRequest(header, payload)
        }
        case _ => {
          println("Cannot handle message : [{}]" + header.name)
          log.error("Cannot handle message : [{}]", header.name)
          None
        }
    }
  }
}