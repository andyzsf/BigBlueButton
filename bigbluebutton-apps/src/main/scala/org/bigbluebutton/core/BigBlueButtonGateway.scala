package org.bigbluebutton.core

import org.bigbluebutton.core.api._
import net.lag.configgy.Configgy
import java.util.concurrent.CountDownLatch
import scala.actors.Actor
import scala.actors.Actor._
import akka.actor.ActorSystem

class BigBlueButtonGateway(outGW: MessageOutGateway, collGW: CollectorGateway) extends SystemConfiguration {
  private val deathSwitch = new CountDownLatch(1)
  // load our config file and configure logfiles.
  Configgy.configure("webapps/bigbluebutton/WEB-INF/configgy-logger.conf")
  // make sure there's always one actor running so scala 2.7.2 doesn't kill off the actors library.
  actor {
	deathSwitch.await
  }
  
  implicit val system = ActorSystem("bigbluebutton-apps-system")
  val bbbActor = system.actorOf(BigBlueButtonActor.props(outGW), "meeting-manager")
  
  def accept(msg: InMessage):Unit = {
    collGW.collectInMessage(msg)
    
    bbbActor ! msg
    
  }

  def acceptKeepAlive(msg: KeepAliveMessage):Unit = {
  	bbbActor ! msg
  }
  
}
