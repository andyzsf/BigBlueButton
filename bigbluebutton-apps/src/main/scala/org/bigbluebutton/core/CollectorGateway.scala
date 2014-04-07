package org.bigbluebutton.core

import org.bigbluebutton.core.api.IDispatcher
import org.bigbluebutton.core.api.InMessage
import org.bigbluebutton.core.api.IOutMessage
import org.bigbluebutton.core.api.OutMessageListener2
import akka.actor.ActorContext
import akka.actor.ActorRef

class CollectorGateway(dispatcher: IDispatcher) extends OutMessageListener2 {

  var actorRef:ActorRef
  
  
  def initialize(context: ActorContext) {
    actorRef = context.actorOf(CollectorActor.props(dispatcher), "dispatcher")
  }
  
  def collectInMessage(msg: InMessage) {
    actorRef ! msg
  }
  
  def handleMessage(msg: IOutMessage) {
    actorRef ! msg
  }
}