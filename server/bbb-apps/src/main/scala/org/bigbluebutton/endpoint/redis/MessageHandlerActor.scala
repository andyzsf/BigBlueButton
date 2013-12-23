package org.bigbluebutton.endpoint.redis

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import org.bigbluebutton.apps.users.protocol.UserJoinRequestMessage
import org.bigbluebutton.apps.users.protocol.UsersMessageHandler

object MessageHandlerActor {
  def props(bbbAppsActor: ActorRef, messageMarshallingActor: ActorRef): Props =  
        Props(classOf[MessageHandlerActor], bbbAppsActor, messageMarshallingActor)
}

class MessageHandlerActor private (val bbbAppsActor: ActorRef, 
            val messageMarshallingActor: ActorRef) extends Actor 
            with ActorLogging with UsersMessageHandler {

  /** RefFactory for actor request-response (ask pattern) **/
  def actorRefFactory = context
  
  def receive = {
    case msg: UserJoinRequestMessage => handleUserJoinRequestMessage(msg)
  }
}