package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import spray.json.JsonParser
import scala.util.Success
import scala.util.Failure
import org.bigbluebutton.apps.protocol.MeetingMessages.CreateMeetingRequest

class UsersAppMessageHandlerSpec extends Specification with ExampleMessageFixtures {

  "The UsersAppMessageHandler" should {
    "returns Failure when passed an invalid request" in {
      MessageTransformer.transformMessage(invalidCreateMeetingRequest) must beFailedTry
    }
  }
    
}