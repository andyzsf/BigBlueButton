package org.bigbluebutton.endpoint.redis

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe, TestActorRef}
import scala.concurrent.duration._
import scala.collection.immutable
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}
import collection.mutable.Stack
import org.bigbluebutton.apps.users.messages.UserJoinRequest

class MessageUnmarshallingActorSpec extends 
  TestKit(ActorSystem("MessageUnmarshallingActorSpec"))
          with DefaultTimeout with ImplicitSender with WordSpecLike 
          with Matchers with BeforeAndAfterAll 
          with UsersMessageTestFixtures {

  val probe = TestProbe()
  val unmarshallingActor =  TestActorRef[MessageUnmarshallingActor](MessageUnmarshallingActor.props(probe.ref))
  
  override def afterAll {
    shutdown(system)
  }
  
  "The MessageUnmarshallingActor" should {
    "Send a UserJoinRequest message when receiving a user join JSON message" in {
      unmarshallingActor ! userJoinMsg
        
      probe.expectMsgPF(500 millis) {
        case ujr:UserJoinRequest => {
          ujr.token == "user1-token-1"
        }            
      }
    }
  }  
}