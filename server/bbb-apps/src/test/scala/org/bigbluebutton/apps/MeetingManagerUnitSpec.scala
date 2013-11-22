package org.bigbluebutton.apps

import akka.testkit.TestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.Matchers
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestProbe
import akka.testkit.TestActorRef
import org.scalatest.FlatSpecLike

class MeetingManagerUnitSpec extends 
  TestKit(ActorSystem("MeetingManagerUnitSpec"))
  with FlatSpecLike 
  with Matchers with BeforeAndAfterAll {

  val pubsub = TestProbe()

  val actorRef = TestActorRef[MeetingManager](Props(classOf[MeetingManager], pubsub.ref))
  val actor = actorRef.underlyingActor
  
  "The MeetingManager" should "return false when a meeting doesn't exist" in {
    assert(actor.meetingExist("foo") === false)
  }
}