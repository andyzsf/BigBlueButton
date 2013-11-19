package org.bigbluebutton

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import scala.concurrent.duration._
import org.bigbluebutton.apps.protocol.MessageHandlerActor
import redis.RedisClient
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global

object Boot extends App with SystemConfiguration {

  implicit val system = ActorSystem("bigbluebutton-apps-system")
 

  val redisPublisherActor = system.actorOf(
                            AppsRedisPublisherActor.props(system), "redis-publisher")
                                
  
  val msgHandler = system.actorOf(Props(classOf[MessageHandlerActor]), "message-handler")
  
  // create and start our service actor
  val service = system.actorOf(Props(classOf[RestEndpointServiceActor], msgHandler), "rest-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = serviceHost, port = servicePort)
  
}