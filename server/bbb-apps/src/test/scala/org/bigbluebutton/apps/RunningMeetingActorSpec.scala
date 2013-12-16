package org.bigbluebutton.apps

import akka.testkit.DefaultTimeout
import akka.testkit.ImplicitSender
import akka.testkit.TestKit
import scala.concurrent.duration._
import scala.collection.immutable
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.WordSpecLike
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import akka.actor.ActorSystem
import collection.mutable.Stack
import akka.actor.Props
import akka.testkit.TestProbe
import org.bigbluebutton.apps.MeetingMessage.CreateMeetingResponse
import org.bigbluebutton.apps.MeetingMessage.MeetingCreated
import org.bigbluebutton.apps.users.data.User
import org.bigbluebutton.apps.users._
import akka.testkit.TestActorRef
import org.bigbluebutton.apps.users.messages.RegisterUserResponse

class RunningMeetingActorSpec extends 
  TestKit(ActorSystem("MeetingManagerSpec"))
  with DefaultTimeout with ImplicitSender with WordSpecLike 
  with Matchers with BeforeAndAfterAll with MeetingManagerTestFixtures {
  
  val pubsub = TestProbe()
  val session = generateMeetingSession()
  val config = generateMeetingDescriptor()
  val runningMeetingActor=  TestActorRef[RunningMeetingActor](RunningMeetingActor.props(pubsub.ref, session, config))
  
  override def afterAll {
    shutdown(system)
  }
  
  "An RunningMeetingActor" should {
    "Respond with 'User has been registered.'" in {
      within(500 millis) {
        val user = generateUserJuan()
        
        runningMeetingActor ! RegisterUserRequest(session, user)
        
        expectMsgPF() {
          case resp:RegisterUserResponse => {
            resp.response.success shouldBe true
            resp.response.message shouldBe "User has been registered."
            runningMeetingActor.underlyingActor.usersApp.isRegistered(resp.token) shouldBe true
            runningMeetingActor.underlyingActor.usersApp.registered.length == 1
          }            
        }
      }
    }
  }

}