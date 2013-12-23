package org.bigbluebutton.apps.users.protocol

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.HeaderAndJsonMessage
import org.bigbluebutton.endpoint.redis.MessageUnmarshallingActor
import org.bigbluebutton.apps.models.Session
import spray.json._
import spray.json.DefaultJsonProtocol._
import org.parboiled.errors.ParsingException
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.users.messages._
import org.bigbluebutton.apps.users.data.Presenter
import org.bigbluebutton.apps.protocol.MessageProcessException

trait UsersMessageUnmarshalling {
  this : MessageUnmarshallingActor =>
  
  val messageHandlerActor: ActorRef
  val log: LoggingAdapter
  
  def handleUserJoin(msg: HeaderAndJsonMessage) = {
    def message(msg: HeaderAndJsonMessage):Option[UserJoinRequestMessage] = {    
      import UserMessagesProtocol._
      try {
        Some(JsonParser(msg.jsonMessage).asJsObject.convertTo[UserJoinRequestMessage])
      }  catch {
        case e: DeserializationException => {
          log.error("Failed to deserialize message: [{}]", msg.jsonMessage)
          None
        } 
        case e: ParsingException => {
          log.error("Invalid JSON Format : [{}]", msg)
          None
        }
      }
    }
        
    message(msg) foreach { userJoinMsg =>
      messageHandlerActor ! userJoinMsg
    }

  }
  
  def handleUserLeave(msg: HeaderAndJsonMessage) = {   
    def message(msg: HeaderAndJsonMessage):Option[UserLeaveMessage] = {    
      import UserMessagesProtocol._
      try {
        Some(JsonParser(msg.jsonMessage).asJsObject.convertTo[UserLeaveMessage])
      }  catch {
        case e: DeserializationException => {
          log.error("Failed to deserialize message: [{}]", msg.jsonMessage)
          None
        } 
        case e: ParsingException => {
          log.error("Invalid JSON Format : [{}]", msg)
          None
        }
      }
    }   

    message(msg) foreach { userLeaveMsg =>
      messageHandlerActor ! userLeaveMsg
    }    
  }
  
  def handleGetUsers(msg: HeaderAndJsonMessage) = {
    def message(msg: HeaderAndJsonMessage):Option[GetUsersRequestMessage] = {    
      import UserMessagesProtocol._
      try {
        Some(JsonParser(msg.jsonMessage).asJsObject.convertTo[GetUsersRequestMessage])
      }  catch {
        case e: DeserializationException => {
          log.error("Failed to deserialize message: [{}]", msg.jsonMessage)
          None
        } 
        case e: ParsingException => {
          log.error("Invalid JSON Format : [{}]", msg)
          None
        }
      }
    }   

    message(msg) foreach { getUsersMsg =>
      messageHandlerActor ! getUsersMsg
    }   
  }
  
  def handleAssignPresenter(msg: HeaderAndJsonMessage) = {
    def message(msg: HeaderAndJsonMessage):Option[AssignPresenterMessage] = {    
      import UserMessagesProtocol._
      try {
        Some(JsonParser(msg.jsonMessage).asJsObject.convertTo[AssignPresenterMessage])
      }  catch {
        case e: DeserializationException => {
          log.error("Failed to deserialize message: [{}]", msg.jsonMessage)
          None
        } 
        case e: ParsingException => {
          log.error("Invalid JSON Format : [{}]", msg)
          None
        }
      }
    }   

    message(msg) foreach { assignPresenterMsg =>
      messageHandlerActor ! assignPresenterMsg
    }     
  }
}