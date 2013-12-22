package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification
import org.parboiled.errors.ParsingException
import spray.json.JsObject
import spray.json.JsValue
import org.bigbluebutton.apps.protocol.Message._

class MessageTransformerSpec extends Specification with ExampleMessageFixtures {

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
    }
}