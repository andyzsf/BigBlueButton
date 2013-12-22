package org.bigbluebutton.endpoint.redis

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import spray.json.{JsObject, JsValue, DefaultJsonProtocol, JsonParser, DeserializationException}
import org.parboiled.errors.ParsingException
import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._
import org.bigbluebutton.apps.protocol._
import scala.util.{Try, Success, Failure}
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.users.unmarshalling.UsersMessageUnmarshaller

object MessageUnmarshallingActor {
  def props(pubsub: ActorRef): Props =  Props(classOf[MessageUnmarshallingActor], pubsub)
}

class MessageUnmarshallingActor private (val pubsub: ActorRef) extends Actor 
         with ActorLogging with UsersMessageUnmarshaller {

  def receive = {
    case msg: String => handleMessage(msg)
    case badMsg => log.error("Unhandled message: [{}", badMsg)
  }
  
  def handleMessage(msg: String) = {
    transformMessage(msg) match {
      case Success(validMsg) => forwardMessage(validMsg)
      case Failure(ex) => log.error("Unhandled message: [{}]", ex)
    }
  }

  def forwardMessage(msg: HeaderAndPayload) = {
    msg.header.event.name match {
      case user_join: String  => handleUserJoin(msg)
	  case unknownMsg => 
	    log.error("Unknown message name: [{}]", unknownMsg)
	}    
  }
    
  def extractMessageHeader(msg: JsObject):Header = {
    try {
      msg.fields.get("header") match {
        case Some(header) => header.convertTo[Header]
        case None => throw MessageProcessException("Cannot get header: [" + msg + "]")
     }
    } catch {
      case e: DeserializationException =>
        throw MessageProcessException("Failed to deserialize header: [" + msg + "]")
    }
  }
 
  def extractPayload(msg: JsObject): JsObject = {
    msg.fields.get("payload") match {
      case Some(payload) => payload.asJsObject
      case None => throw MessageProcessException("Cannot get payload information: [" + msg + "]")
    } 
  }
  
  def jsonMessageToObject(msg: String): JsObject = {
    log.debug("Converting to json : {}", msg)    
    try {
      JsonParser(msg).asJsObject
    } catch {
      case e: ParsingException => {
        log.error("Cannot parse message: {}", msg)
        throw MessageProcessException("Cannot parse JSON message: [" + msg + "]")
      }
    }
  }

  def toHeaderAndPayload(header: Header, payload:JsObject):HeaderAndPayload = {
    HeaderAndPayload(header, payload)
  }
    
  def transformMessage(jsonMsg: String):Try[HeaderAndPayload] = {
    for {
      jsonObj <- Try(jsonMessageToObject(jsonMsg))
      header <- Try(extractMessageHeader(jsonObj))
      payload <- Try(extractPayload(jsonObj))
      message = toHeaderAndPayload(header, payload)
    } yield message
  }
  
  def extractSession(header: Header):Option[Session] = {
    header.meeting.session match {
      case Some(sessionId) => {
             Some(Session(sessionId, header.meeting.id,
                          header.meeting.name))
        }
        case None => None
      }
  }
}