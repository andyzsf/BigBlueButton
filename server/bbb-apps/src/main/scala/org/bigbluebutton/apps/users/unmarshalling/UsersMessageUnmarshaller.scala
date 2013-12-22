package org.bigbluebutton.apps.users.unmarshalling
import akka.actor.ActorRef
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import org.bigbluebutton.endpoint.redis.MessageUnmarshallingActor


trait UsersMessageUnmarshaller {
  this : MessageUnmarshallingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  
  def handleUserJoin(msg: HeaderAndPayload) = {
    
  }
}