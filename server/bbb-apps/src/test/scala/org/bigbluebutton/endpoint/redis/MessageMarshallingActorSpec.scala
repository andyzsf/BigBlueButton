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
import org.bigbluebutton.apps.users.messages.GetUsersRequest
import org.bigbluebutton.apps.users.messages.AssignPresenter
import spray.json._
import spray.json.DefaultJsonProtocol._
import org.bigbluebutton.endpoint.MessageMarshallingActor

class MessageMarshallingActorSpec extends 
  TestKit(ActorSystem("MessageMarshallingActorSpec"))
          with DefaultTimeout with ImplicitSender with WordSpecLike 
          with Matchers with BeforeAndAfterAll 
          with UsersMessageTestFixtures {

  val pubsubProbe = TestProbe()
  val marshallingActor =  TestActorRef[MessageMarshallingActor](
                                   MessageMarshallingActor.props(
                                   pubsubProbe.ref))
  
  override def afterAll {
    shutdown(system)
  }
  
  "The MessageMarshallingActor" should {
    "Send a UserJoinResponse message when receiving a user join JSON message" in {
      marshallingActor ! userJoinResponseMessage
        
      pubsubProbe.expectMsgPF(500 millis) {
        case ujr:JsonMessage => {
          ujr.message should include (juanExtUserId)
        }            
        case _ => fail("Expected a JsonMessage message.")
      }
    }
       
  }  
}