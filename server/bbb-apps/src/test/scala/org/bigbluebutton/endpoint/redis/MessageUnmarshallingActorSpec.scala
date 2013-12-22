package org.bigbluebutton.endpoint.redis

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe, TestActorRef}
import scala.concurrent.duration._
import scala.collection.immutable
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}
import collection.mutable.Stack
import org.bigbluebutton.apps.users.messages.UserJoinRequest
import org.bigbluebutton.apps.users.messages.UserLeave

class MessageUnmarshallingActorSpec extends 
  TestKit(ActorSystem("MessageUnmarshallingActorSpec"))
          with DefaultTimeout with ImplicitSender with WordSpecLike 
          with Matchers with BeforeAndAfterAll 
          with UsersMessageTestFixtures {

  val bbbAppsProbe = TestProbe()
  val pubsubProbe = TestProbe()
  val unmarshallingActor =  TestActorRef[MessageUnmarshallingActor](
                                   MessageUnmarshallingActor.props(
                                   bbbAppsProbe.ref, pubsubProbe.ref))
  
  override def afterAll {
    shutdown(system)
  }
  
  "The MessageUnmarshallingActor" should {
    "Send a UserJoinRequest message when receiving a user join JSON message" in {
      unmarshallingActor ! userJoinMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:UserJoinRequest => {
          ujr.token == "user1-token-1"
        }            
      }
    }
    
    "Send a UserLeave message when receiving a user leave JSON message" in {
      unmarshallingActor ! userLeaveMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:UserLeave => {
          ujr.userId == "user1"
        }            
      }
    }
  }  
}