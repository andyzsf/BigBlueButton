package org.bigbluebutton.apps.users.unmarshalling
import akka.actor.ActorRef
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import org.bigbluebutton.endpoint.redis.MessageUnmarshallingActor
import org.bigbluebutton.apps.models.Session
import spray.json.JsObject
import org.bigbluebutton.apps.protocol.MessageProcessException
import scala.util.Try
import spray.json.JsValue
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.users.messages.UserJoinRequest
import org.bigbluebutton.apps.users.messages.UserLeave

trait UsersMessageUnmarshaller {
  this : MessageUnmarshallingActor =>
  
  val bbbAppsActor: ActorRef
  val pubsubActor: ActorRef
  val log: LoggingAdapter
  
  def handleUserJoin(msg: HeaderAndPayload) = {
    def message(token: String, session: Session):UserJoinRequest = {    
      UserJoinRequest(session, token)
    }
    
    val userJoinMsg = for {
      token <- msg.payload.asJsObject.fields.get("token")
      session <- toSession(msg.header)
      userJoinMsg = message(token.toString, session)
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
    
    val userLeaveMsg = for {
      userId <- msg.payload.asJsObject.fields.get("user_id")
      session <- toSession(msg.header)
      userLeaveMsg = message(userId.toString, session)
    } yield userLeaveMsg
    
    userLeaveMsg match { 
      case Some(ulm) => {
        bbbAppsActor ! ulm
      }
      case None => log.error("Failed to handle UserLeave message [{}]", msg.payload)
    }    
  }
}