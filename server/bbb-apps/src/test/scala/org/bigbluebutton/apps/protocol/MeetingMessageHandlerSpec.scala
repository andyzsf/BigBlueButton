package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import spray.json.JsonParser
import scala.util.Success
import scala.util.Failure

class MeetingMessageHandlerSpec extends Specification {
  val createMeetingRequest = """
{
    "header": {
        "name": "CreateMeetingRequest",
        "timestamp": 123456,
        "source": "web-api",
        "correlation": "123abc"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "externalId": "english_101",
            "record": true,
            "welcomeMessage": "Welcome to English 101",
            "logoutUrl": "http://www.bigbluebutton.org",
            "avatarUrl": "http://www.gravatar.com/bigbluebutton",
            "users": {
                "max": 20,
                "hardLimit": false
            },
            "duration": {
                "length": 120,
                "allowExtend": false,
                "warnBefore": 30
            },
            "voiceConf": {
                "pin": 123456,
                "number": 85115
            },
            "phoneNumbers": [
                {
                    "number": "613-520-7600",
                    "description": "Ottawa"
                },
                {
                    "number": "1-888-555-7890",
                    "description": "NA Toll-Free"
                }
            ],
            "metadata": {
                "customerId": "acme-customer",
                "customerName": "ACME"
            }
        }
    }
}  
    """

  val invalidCreateMeetingRequest = """
{
    "header": {
        "name": "CreateMeeting",
        "timestamp": 123456,
        "correlation": "123abc",
        "source": "web-api"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "externalId": "english_101",
            "record": true,
            "welcomeMessage": "Welcome to English 101",
            "logoutUrl": "http://www.bigbluebutton.org",
            "avatarUrl": "http://www.gravatar.com/bigbluebutton",
            "users": {
                "max": 20,
                "hardLimit": false
            },
            "duration": {
                "length": 120,
                "allowExtend": false,
                "warnBefore": 30
            },
            "voiceConf": {
                "pin": 123456,
                "number": 85115
            },
            "phoneNumbers": [
                {
                    "number": "613-520-7600",
                    "description": "Ottawa"
                },
                {
                    "number": "1-888-555-7890",
                    "description": "NA Toll-Free"
                }
            ],
            "metadata": [
               {"customerId": "acme-customer"},
               {"customerName": "ACME"}
            ]
        }
    }
}  
    """
    
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