package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.JsValue
import spray.json.DefaultJsonProtocol
import spray.json.JsonParser
import org.parboiled.errors.ParsingException
import InMessageNameContants._
import akka.event.LoggingAdapter
import akka.event.slf4j.SLF4JLogging
import scala.util.Try
import org.bigbluebutton.apps.protocol.MessageProcessException

object ExtractMessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val headerFormat = jsonFormat4(Header)
}

object MessageTransformer extends MeetingMessageHandler with SLF4JLogging {
  
  import ExtractMessageHeaderJsonProtocol._
  
  def extractMessageHeader(msg: JsObject):Header = {
    msg.fields.get("header") match {
      case Some(header) => {
        header.convertTo[Header]
      }
      case None => throw MessageProcessException("Cannot get header information")
    }
  }
 
  def extractPayload(msg: JsObject): JsValue = {
    msg.fields.get("payload") match {
      case Some(payload) => payload
      case None => throw MessageProcessException("Cannot get payload information")
    } 
  }
  
  def jsonMessageToObject(msg: String): JsObject = {
    log.debug("Converting to json : {}", msg)
    
    try {
      JsonParser(msg).asJsObject
    } catch {
      case e: ParsingException => {
        log.error("Cannot parse message: {}", msg)
        throw MessageProcessException("Cannot parse JSON message: " + msg)
      }
    }
  }
    
  def transformMessage(jsonMsg: String):Try[InMessage] = {
    val result = Try({
	    val jsonObj = jsonMessageToObject(jsonMsg)
	    val header = extractMessageHeader(jsonObj)
	    val payload = extractPayload(jsonObj)	  
	    Try(processMessage(header, payload.asJsObject))
      }
    )
    
    result.flatMap(f)
  }
  
  def processMessage(header: Header, payload:JsObject):Try[InMessage] = {
    Try(header.name match {
	        case CreateMeetingRequestMessage  => {
	          handleCreateMeetingRequest(header, payload)
	        }
	        case _ => {
	          throw MessageProcessException("Unknown message name : " + header.name)
	        }
	    })
  }
}