package org.bigbluebutton

import akka.actor.Props
import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}
import redis.RedisClient
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorRef
import org.bigbluebutton.apps.protocol.MessageTransformer
import akka.event.slf4j.SLF4JLogging

object AppsRedisSubscriberActor extends SystemConfiguration {

  val channels = Seq("time")
  val patterns = Seq("pattern.*")
  
  def props(bbbAppsActor: ActorRef): Props = 
	      Props(classOf[AppsRedisSubscriberActor], 
	            bbbAppsActor, redisHost, redisPort, channels, patterns).
	      withDispatcher("rediscala.rediscala-client-worker-dispatcher")
}

class AppsRedisSubscriberActor(bbbAppsActor: ActorRef, redisHost: String, 
                 redisPort: Int, 
                 channels: Seq[String] = Nil, patterns: Seq[String] = Nil)
                 extends RedisSubscriberActor(
                      new InetSocketAddress(redisHost, redisPort), 
                      channels, patterns) {

  def onMessage(message: Message) {
    log.debug(s"message received: $message")
  }

  def onPMessage(pmessage: PMessage) {
    log.debug(s"pattern message received: $pmessage")
  }
  
  def handleMessage(msg: String) {
    val transformedMessage = MessageTransformer.transformMessage(msg)
    
    /**
     * TODO: Inspect the message and determine if we should expect a reply
     * or not. If we need a reply, use Future to send the message and handle
     * the reply.
     */
    if (transformedMessage != None) {
      bbbAppsActor ! transformedMessage
    }
  }
}