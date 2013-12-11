package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import spray.json.JsonParser
import scala.util.Success
import scala.util.Failure
import org.bigbluebutton.apps.protocol.MeetingMessages.CreateMeetingRequest

class MeetingMessageHandlerSpec extends Specification with ExampleMessageFixtures {

  "The MeetingMessageHandler" should {
    "returns the message when passed a valid request" in {
      val result =  MessageTransformer.transformMessage(createMeetingRequest) 
      result match {
        case Success(message) => 
          message must beAnInstanceOf[CreateMeetingRequest]  
        case Failure(_) => 
          throw new Exception("We should have received a CreateMeetingRequest instance.")
      }
    }
    "returns Failure when passed an invalid request" in {
      MessageTransformer.transformMessage(invalidCreateMeetingRequest) must beFailedTry
    }
  }
    
}