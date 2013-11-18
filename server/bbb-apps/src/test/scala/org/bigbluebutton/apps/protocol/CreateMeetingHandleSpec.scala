package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import spray.json.JsonParser

class CreateMeetingHandleSpec extends Specification {

  val createMeetingRequest = """
{
    "header": {
        "name": "CreateMeeting",
        "timestamp": 123456,
        "correlationId": "123abc",
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
        "correlationId": "123abc",
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
        
  "The CreateMeetingMessage Handler" should {
    "returns the message when passed a valid request" in {
      val jsonAst = JsonParser(createMeetingRequest)
      val jsonObj = jsonAst.asJsObject
      val payloadObj = jsonObj.fields.get("payload").get.asJsObject
      
      CreateMessageHandler.processCreateMeetingMessage(payloadObj) must beSome
    }
    
    "returns None when passed an invalid request" in {
      val jsonAst = JsonParser(invalidCreateMeetingRequest)
      val jsonObj = jsonAst.asJsObject
      val payloadObj = jsonObj.fields.get("payload").get.asJsObject
      
      CreateMessageHandler.processCreateMeetingMessage(payloadObj) must beNone
    }
  }
}