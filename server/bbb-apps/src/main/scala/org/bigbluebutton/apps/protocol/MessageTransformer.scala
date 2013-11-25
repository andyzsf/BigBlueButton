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
import spray.json.DeserializationException

object ExtractMessageHeaderJsonProtocol extends DefaultJsonProtocol {
  implicit val headerFormat = jsonFormat4(Header)
}

object MessageTransformer extends MeetingMessageHandler with SLF4JLogging {  
  import ExtractMessageHeaderJsonProtocol._
  
  /**
   * Extract the header from the message.
   * 
   * @ returns the header is successful
   */
  def extractMessageHeader(msg: JsObject):Header = {
    try {
      msg.fields.get("header") match {
        case Some(header) => header.convertTo[Header]
        case None => throw MessageProcessException("Cannot get header : " + msg)
     }
    } catch {
      case e: DeserializationException =>
        throw MessageProcessException("Failed to deserialize header : " + msg)
    }
  }
 
  /**
   * Extract the payload from the message.
   * 
   * @returns the payload if successful
   */
  def extractPayload(msg: JsObject): JsObject = {
    msg.fields.get("payload") match {
      case Some(payload) => payload.asJsObject
      case None => throw MessageProcessException("Cannot get payload information")
    } 
  }
  
  /**
   * Converts a JSON string into JsObject.
   */
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

  def processMessage(header: Header, payload:JsObject):Try[InMessage] = {
    Try(determineMessage(header, payload))
  }
    
  /**
   * Transforms the JSON string message into an InMessage
   */
  def transformMessage(jsonMsg: String):Try[InMessage] = {
    for {
      jsonObj <- Try(jsonMessageToObject(jsonMsg))
      header <- Try(extractMessageHeader(jsonObj))
      payload <- Try(extractPayload(jsonObj))
      message <- processMessage(header, payload)
    } yield message
  }
  
  /**
   * Determines the type of message that was sent.
   * 
   * @returns an instance of the message received.
   */
  def determineMessage(header: Header, payload:JsObject):InMessage = {
    header.name match {
      case CreateMeetingRequestMessage  => 
        handleCreateMeetingRequest(header, payload)
	  case _ => 
	    throw MessageProcessException("Unknown message name : " + header.name)
	}
  }
  

}