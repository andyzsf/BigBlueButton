package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import org.parboiled.errors.ParsingException
import spray.json.JsObject
import spray.json.JsValue
import org.bigbluebutton.apps.protocol.Message._

class MessageHandleSpec extends Specification {
  val invalidJSON = """{ "invalid"  "Missing a colon" }"""
    
  val validJSON = """{ "valid" : "Valid JSON" }"""
  
  val invalidMessage = """ 
			{
			  "header": {
			    "name": "PrivateChatMessageEvent",
			    "timestamp": 123456
			  },
			  "payload1" : {
			    { "valid" : "Valid JSON" }
			  }
			}    
    
    """
  val validMessage = """
			{
			  "header": {
			    "name": "PrivateChatMessageEvent",
			    "timestamp": 123456,
			    "meeting": {
			        "id": "english_101",
			        "name": "English 101",
			        "session": "183f0bf3a0982a127bdb8161e0c44eb696b3e75c-1383210136298"
			    }
			  },
			  "payload" : {
			    { "valid" : "Valid JSON" }
			  }
			}  
  """
    
    "The Message Handler" should {
      "returns None when passed an invalid JSON" in {
        MessageHandler.processMessage(invalidJSON) must beNone
      }
      "returns a Some(JsObject) when able to parse a valid JSON" in {
        MessageHandler.processMessage(validJSON) must beSome
      }
/*      "returns a Some(MessageHeader) when able to get the message header" in {
        MessageHandler.processMessage(validMessage) foreach { msg => 
          MessageHandler.extractMessageHeader(msg) must beSome 
        }
      }
      "returns a None when unable to get the message header" in {
        MessageHandler.processMessage(invalidMessage) foreach { msg => 
          MessageHandler.extractMessageHeader(msg) must beNone 
        } 
      }
      "returns a Some(JsValue) when able to get the payload from the message" in {
        MessageHandler.processMessage(validMessage) foreach { msg => 
          MessageHandler.extractPayload(msg) must beSome 
        }
      }
      "returns a None when unable to get the payload from the message" in {
        MessageHandler.processMessage(invalidMessage) foreach { msg => 
          MessageHandler.extractPayload(msg) must beNone 
        }
      }
*/      
    }
}