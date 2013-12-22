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

trait UsersMessageUnmarshaller {
  this : MessageUnmarshallingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  
 
  def handleUserJoin(msg: HeaderAndPayload) = {
    def message(token: String, session: Session):UserJoinRequest = {    
      UserJoinRequest(session, token)
    }
    
    val userJoinMsg = for {
      token <- msg.payload.asJsObject.fields.get("token")
      session <- extractSession(msg.header)
      userJoinMsg = message(token.toString, session)
    } yield userJoinMsg
    
    userJoinMsg match { 
      case Some(ujm) => {
        log.debug("Forwarding user join message")
        pubsub ! ujm
      }
      case None => log.error("Failed to handle UserJoin message [{}]", msg.payload)
    }
  }
}