package org.bigbluebutton.endpoint

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import org.bigbluebutton.apps.users.protocol.UsersMessageMarshalling
import org.bigbluebutton.endpoint.UserJoinResponseMessage

object MessageMarshallingActor {
  def props(pubsubActor: ActorRef): Props =  
        Props(classOf[MessageMarshallingActor], pubsubActor)
}

class MessageMarshallingActor private (val pubsubActor: ActorRef) extends Actor 
         with ActorLogging with UsersMessageMarshalling {

  def receive = {
    case msg: UserJoinResponseMessage => marshallUserJoinResponseMessage(msg)
    
    case unknownMsg => log.error("Cannot marshall unknown message: [{}]", unknownMsg)
  }
}