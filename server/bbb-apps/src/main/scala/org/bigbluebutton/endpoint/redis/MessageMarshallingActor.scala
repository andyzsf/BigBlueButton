package org.bigbluebutton.endpoint.redis

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import spray.json.{JsObject, JsValue, DefaultJsonProtocol, JsonParser, DeserializationException}
import org.bigbluebutton.apps.users.protocol.UsersMessageMarshalling
import org.bigbluebutton.apps.users.messages.UserJoined

object MessageMarshallingActor {
  def props(pubsubActor: ActorRef): Props =  
        Props(classOf[MessageMarshallingActor], pubsubActor)
}

class MessageMarshallingActor private (val pubsubActor: ActorRef) extends Actor 
         with ActorLogging with UsersMessageMarshalling {

  def receive = {
    case msg: UserJoined => marshallUserJoined(msg)
    
    case unknownMsg => log.error("Cannot marshall unknow message: [{}]", unknownMsg)
  }
}