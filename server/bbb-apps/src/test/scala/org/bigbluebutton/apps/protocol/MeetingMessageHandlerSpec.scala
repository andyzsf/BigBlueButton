package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import spray.json.JsonParser
import scala.util.Success
import scala.util.Failure
import org.bigbluebutton.apps._

class MeetingMessageHandlerSpec extends Specification with ExampleMessageFixtures {

  "The MeetingMessageHandler" should {
    "returns Failure when passed an invalid request" in {
      MessageTransformer.transformMessage(invalidCreateMeetingRequest) must beFailedTry
    }
  }
    
}