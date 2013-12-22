package org.bigbluebutton.apps.users.protocol

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import spray.json._
import spray.json.DefaultJsonProtocol._

import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.endpoint.redis.MessageMarshallingActor
import org.bigbluebutton.apps.models.Session

object UserJoinedProtocol extends DefaultJsonProtocol {
  import UserMessagesProtocol._
  
  implicit val sessionFormat = jsonFormat3(Session)
  implicit val userJoinedFormat = jsonFormat3(UserJoined)  
}

trait UsersMessageMarshalling {
  this : MessageMarshallingActor =>
  
  val pubsubActor: ActorRef
  val log: LoggingAdapter
  
  def marshallUserJoined(msg: UserJoined) = {
    def toJsObject(msg: UserJoined):JsObject = {
      import UserJoinedProtocol._
      msg.toJson.asJsObject      
    }
        
    pubsubActor ! toJsObject(msg) 
  }
}