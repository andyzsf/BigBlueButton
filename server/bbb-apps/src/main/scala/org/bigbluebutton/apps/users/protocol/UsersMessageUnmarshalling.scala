package org.bigbluebutton.apps.users.protocol

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import org.bigbluebutton.endpoint.redis.MessageUnmarshallingActor
import org.bigbluebutton.apps.models.Session
import spray.json._
import spray.json.DefaultJsonProtocol._
import org.bigbluebutton.apps.protocol.MessageProcessException
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.users.messages._
import org.bigbluebutton.apps.users.data.Presenter
import org.bigbluebutton.apps.protocol.MessageProcessException

trait UsersMessageUnmarshalling {
  this : MessageUnmarshallingActor =>
  
  val bbbAppsActor: ActorRef
  val pubsubActor: ActorRef
  val log: LoggingAdapter
  
  def handleUserJoin(msg: HeaderAndPayload) = {
    def message(token: String, session: Session):UserJoinRequest = {    
      UserJoinRequest(session, token)
    }
    
    /**
     * Need this to convert JsValue to String
     */
    def token(payload: JsObject):Option[String] = {
      payload.fields.get("token") match {
        case Some(s) => Some(s.convertTo[String])
        case None => None
      }
    }
    
    val userJoinMsg = for {
      token <- token(msg.payload.asJsObject)
      session <- toSession(msg.header)
      userJoinMsg = message(token, session)
    } yield userJoinMsg
    
    userJoinMsg match { 
      case Some(ujm) => {
        bbbAppsActor ! ujm
      }
      case None => log.error("Failed to handle UserJoin message [{}]", msg.payload)
    }
  }
  
  def handleUserLeave(msg: HeaderAndPayload) = {
    def message(userId: String, session: Session):UserLeave = {    
      UserLeave(session, userId)
    }    

    /**
     * Need this to convert JsValue to String
     */
    def userId(payload: JsObject):Option[String] = {
      payload.fields.get("user_id") match {
        case Some(s) => Some(s.convertTo[String])
        case None => None
      }
    }
        
    val userLeaveMsg = for {
      userId <- userId(msg.payload.asJsObject)
      session <- toSession(msg.header)
      userLeaveMsg = message(userId, session)
    } yield userLeaveMsg
    
    userLeaveMsg match { 
      case Some(ulm) => {
        bbbAppsActor ! ulm
      }
      case None => log.error("Failed to handle UserLeave message [{}]", msg.payload)
    }    
  }
  
  def handleGetUsers(msg: HeaderAndPayload) = {
    def message(requesterId: String, session: Session):GetUsersRequest = {    
      GetUsersRequest(session, requesterId)
    }    

    /**
     * Need this to convert JsValue to String
     */
    def requesterId(payload: JsObject):Option[String] = {
      payload.fields.get("requester_id") match {
        case Some(s) => Some(s.convertTo[String])
        case None => None
      }
    }
    
    val getUsersMsg = for {
      requesterId <- requesterId(msg.payload.asJsObject)
      session <- toSession(msg.header)
      getUsersMsg = message(requesterId.toString, session)
    } yield getUsersMsg
    
    getUsersMsg match { 
      case Some(gum) => {
        bbbAppsActor ! gum
      }
      case None => log.error("Failed to handle GetUsers message [{}]", msg.payload)
    }    
  }
  
  def handleAssignPresenter(msg: HeaderAndPayload) = {
    def message(session: Session, payload: PresenterPayload):AssignPresenter = {
      val presenter = Presenter(payload.presenter, payload.assigned_by)
      AssignPresenter(session, presenter)
    }    
    
    def convertToPresenter(payload: JsObject):Option[PresenterPayload] = {
      import UserMessagesProtocol._
      try {
        Some(payload.convertTo[PresenterPayload])
      }  catch {
        case e: DeserializationException => {
          log.error("Failed to deserialize Presenter: [{}]", payload)
          None
        }          
      }
    }
    
    val assignMsg = for {
      payload <- convertToPresenter(msg.payload.asJsObject)
      session <- toSession(msg.header)
      assignMsg = message(session, payload)
    } yield assignMsg
    
    assignMsg match { 
      case Some(apm) => {
        bbbAppsActor ! apm
      }
      case None => log.error("Failed to handle AssignPresenter message [{}]", msg.payload)
    }    
  }
}