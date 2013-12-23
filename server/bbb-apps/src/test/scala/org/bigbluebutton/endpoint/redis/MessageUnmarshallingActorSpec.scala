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

class MessageUnmarshallingActorSpec extends 
  TestKit(ActorSystem("MessageUnmarshallingActorSpec"))
          with DefaultTimeout with ImplicitSender with WordSpecLike 
          with Matchers with BeforeAndAfterAll 
          with UsersMessageTestFixtures {

  val bbbAppsProbe = TestProbe()
  val unmarshallingActor =  TestActorRef[MessageUnmarshallingActor](
                                   MessageUnmarshallingActor.props(
                                   bbbAppsProbe.ref))
  
  override def afterAll {
    shutdown(system)
  }
  
  "The MessageUnmarshallingActor" should {
    "Send a UserJoinRequest message when receiving a user join JSON message" in {
      unmarshallingActor ! userJoinMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:UserJoinRequest => {
          ujr.token should be ("user1-token-1") 
        }     
        case _ => fail("Should have returned UserJoinRequest")
      }
    }
    
    "Send a UserLeave message when receiving a user leave JSON message" in {
      unmarshallingActor ! userLeaveMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:UserLeave => {
          ujr.userId should be ("user1")
        }        
        case _ => fail("Should have returned UserLeave")
      }
    }
    
    "Send a GetUsers message when receiving a get users JSON message" in {
      unmarshallingActor ! getUsersMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:GetUsersRequest => {
          ujr.requesterId should be ("user1")
        }       
        case _ => fail("Should have returned GetUsersRequest")
      }
    }
    
    "Send an AssignPresenter message when receiving assign presenter JSON message" in {
      unmarshallingActor ! assignPresenterMsg
        
      bbbAppsProbe.expectMsgPF(500 millis) {
        case ujr:AssignPresenter => {
          ujr.presenter.presenter.id should be ("user1")
        }     
        case _ => fail("Should have returned AssignPresenter")
      }
    }    
  }  
}