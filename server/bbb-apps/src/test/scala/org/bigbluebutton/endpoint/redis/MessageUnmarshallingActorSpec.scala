package org.bigbluebutton.endpoint.redis

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit, TestProbe, TestActorRef}
import scala.concurrent.duration._
import scala.collection.immutable
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{WordSpecLike, BeforeAndAfterAll, Matchers}
import collection.mutable.Stack
import org.bigbluebutton.apps.users.protocol.UserJoinRequestMessage
import org.bigbluebutton.apps.users.protocol.UserLeaveMessage
import org.bigbluebutton.apps.users.protocol.GetUsersRequestMessage
import org.bigbluebutton.apps.users.protocol.AssignPresenterMessage

class MessageUnmarshallingActorSpec extends 
  TestKit(ActorSystem("MessageUnmarshallingActorSpec"))
          with DefaultTimeout with ImplicitSender with WordSpecLike 
          with Matchers with BeforeAndAfterAll 
          with UsersMessageTestFixtures with UsersMessageJsonTestFixtures {

  val messageHandlerProbe = TestProbe()
  val unmarshallingActor =  TestActorRef[MessageUnmarshallingActor](
                                   MessageUnmarshallingActor.props(
                                   messageHandlerProbe.ref))
  
  override def afterAll {
    shutdown(system)
  }
  
  "The MessageUnmarshallingActor" should {
    "Send a UserJoinRequest message when receiving a user join JSON message" in {
      unmarshallingActor ! userJoinMsg
        
      messageHandlerProbe.expectMsgPF(500 millis) {
        case ujr:UserJoinRequestMessage => {
          ujr.payload.token should be ("user1-token-1") 
        }     
        case _ => fail("Should have returned UserJoinRequestMessage")
      }
    }
    
    "Send a UserLeave message when receiving a user leave JSON message" in {
      unmarshallingActor ! userLeaveMsg
        
      messageHandlerProbe.expectMsgPF(500 millis) {
        case ujr:UserLeaveMessage => {
          ujr.payload.user.id should be ("user1")
        }        
        case _ => fail("Should have returned UserLeaveMessage")
      }
    }
    
    "Send a GetUsers message when receiving a get users JSON message" in {
      unmarshallingActor ! getUsersMsg
        
      messageHandlerProbe.expectMsgPF(500 millis) {
        case ujr:GetUsersRequestMessage => {
          ujr.payload.requester.id should be ("user1")
        }       
        case _ => fail("Should have returned GetUsersRequest")
      }
    }
    
    "Send an AssignPresenter message when receiving assign presenter JSON message" in {
      unmarshallingActor ! assignPresenterMsg
        
      messageHandlerProbe.expectMsgPF(500 millis) {
        case ujr:AssignPresenterMessage => {
          ujr.payload.presenter.id should be ("user1")
        }     
        case _ => fail("Should have returned AssignPresenter")
      }
    }    
  }  
}