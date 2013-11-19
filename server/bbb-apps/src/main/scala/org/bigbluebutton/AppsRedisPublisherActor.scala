package org.bigbluebutton

import akka.actor.Props
import java.net.InetSocketAddress
import redis.actors.RedisSubscriberActor
import redis.api.pubsub.{PMessage, Message}
import redis.RedisClient
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.ActorSystem
import scala.concurrent.{Future, Await}
import akka.actor.Actor

object AppsRedisPublisherActor extends SystemConfiguration {
 
  val channels = Seq("time")
  val patterns = Seq("pattern.*")
  
  def props(system: ActorSystem): Props = 
	      Props(classOf[AppsRedisPublisherActor], 
	            system, redisHost, redisPort)
}

class AppsRedisPublisherActor(val system: ActorSystem, 
    val host: String, val port: Int) extends Actor {
  
  val redis = RedisClient(host, port)(system)

  val futurePong = redis.ping()
  println("Ping sent!")
  futurePong.map(pong => {
    println(s"Redis replied with a $pong")
  })
  Await.result(futurePong, 5 seconds)
  
  def publish(msg: String) {
    redis.publish("time", System.currentTimeMillis())
  }
  
  def receive = {
    case rxMsg => println("PUBLISH TO REDIS " + rxMsg)
  }

}
