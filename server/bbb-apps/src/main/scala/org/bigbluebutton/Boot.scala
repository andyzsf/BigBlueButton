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

  implicit val system = ActorSystem("bigbluebutton-apps")

//  val camelExtention = CamelExtension(system)
  
//  val activated = camelExtention.activationFutureFor(consumer) (timeout = 10 seconds, executor = system.dispatcher)
  
    val redis = RedisClient()

  val futurePong = redis.ping()
  println("Ping sent!")
  futurePong.map(pong => {
    println(s"Redis replied with a $pong")
  })
  Await.result(futurePong, 5 seconds)
  
  val redisActor = new RedisPubSubActor
  
  val msgHandler = system.actorOf(Props(classOf[MessageHandlerActor]), "message-handler")
  
  // create and start our service actor
  val service = system.actorOf(Props(classOf[RestEndpointServiceActor], msgHandler), "rest-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = serviceHost, port = servicePort)
  
}