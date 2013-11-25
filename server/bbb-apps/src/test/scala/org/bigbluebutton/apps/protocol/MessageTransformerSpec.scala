package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import org.parboiled.errors.ParsingException
import spray.json.JsObject
import spray.json.JsValue
import org.bigbluebutton.apps.protocol.Message._

class MessageTransformerSpec extends Specification {
  val invalidJSON = """{ "invalid"  "Missing a colon" }"""
    
  val validJSON = """{ "valid" : "Valid JSON" }"""
  
  val invalidMessage = """ 
{
    "header": {
        "name": "PrivateChatMessageEvent",
        "timestamp": 123456
    },
    "payload1": {
        "valid": "Valid JSON"
    }
}       
    """
    
  val validMessage = """
{
    "header": {
        "name": "CreateMeetingRequest",
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
            "metadata": {
                "customerId": "acme-customer",
                "customerName": "ACME"
            }
        }
    }
}   
  """
    
    "The MessageTransformer" should {
      "throw an exception when passed an invalid JSON" in {
        MessageTransformer.jsonMessageToObject(invalidJSON) must throwA[MessageProcessException]
      }
      "returns a JsObject when able to parse a valid JSON" in {
        MessageTransformer.jsonMessageToObject(validJSON) must haveClass[JsObject]
      }
      "throw an exception when unable to get the message header" in {
        val jsObject = MessageTransformer.jsonMessageToObject(invalidMessage)
        MessageTransformer.extractMessageHeader(jsObject) must throwA[MessageProcessException]
      }
      "returns a Header when passed a valid message" in {
        val jsObject = MessageTransformer.jsonMessageToObject(validMessage)
        MessageTransformer.extractMessageHeader(jsObject) must haveClass[Header]
      }
      "throws an exception when unable to get the payload from the message" in {
        val jsObject = MessageTransformer.jsonMessageToObject(invalidMessage)
        MessageTransformer.extractPayload(jsObject) must throwA[MessageProcessException]
      }
      "returns a JsObject when able to get the payload from the message" in {
        val jsObject = MessageTransformer.jsonMessageToObject(validMessage)
        MessageTransformer.extractPayload(jsObject) must haveClass[JsObject]
      }   
      "returns a CreateMeeting message" in {
       MessageTransformer.transformMessage(validMessage) must beSuccessfulTry 
      }
    }
}