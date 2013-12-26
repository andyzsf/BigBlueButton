package org.bigbluebutton.endpoint

import org.bigbluebutton.apps.UnitSpec
import spray.json.JsonParser
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.json.lenses.JsonLenses._
import spray.json.lenses._
import org.bigbluebutton.endpoint.UserMessagesProtocol._

class UsersMessageJsonConversionSpec extends UnitSpec with UsersMessageJsonTestFixtures {
   
  "A user_join_response Message" should "have a user field" in {
    val ujrm = JsonParser(user_join_response_Message)
    val message = ujrm.convertTo[UserJoinResponseFormat]
    
    message.payload.user match {
      case Some(u) => u.id should be (juanUserId)
      case None => fail("Should have user field")
    }

    import spray.json.DefaultJsonProtocol._
    val jsonMessage = message.toJson
    val userIdLens = ("payload" / "user" / "id")
    val userId = jsonMessage.extract[String](userIdLens)
    userId should be (juanUserId)
  }

  
}