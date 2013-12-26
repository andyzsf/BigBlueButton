package org.bigbluebutton.endpoint

import org.bigbluebutton.apps.UnitSpec
import spray.json.JsonParser
import spray.json._
import spray.json.DefaultJsonProtocol._
import spray.json.lenses.JsonLenses._
import spray.json.lenses._
import org.bigbluebutton.endpoint.UserMessagesProtocol._
import org.bigbluebutton.apps.AppsTestFixtures

class UsersMessageJsonConversionSpec extends UnitSpec 
       with JsonMessagesFixtures with AppsTestFixtures {
   
  "A user_join_response Message" should "be un/marshalled" in {
    val ujrm = JsonParser(UserJoinResponseJson)
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

  "A CreateMeetingRequestJson Message" should "be un/marshalled" in {
    val ujrm = JsonParser(CreateMeetingRequestJson)
    val message = ujrm.convertTo[CreateMeetingRequestMessage]
    
    message.payload.meeting_descriptor.external_id should be (eng101MeetingId)

    import spray.json.DefaultJsonProtocol._
    val jsonMessage = message.toJson
    val meetingIdLens = ("payload" /"meeting_descriptor" / "external_id")
    val meetingId = jsonMessage.extract[String](meetingIdLens)
    meetingId should be (eng101MeetingId)
  }

  "A CreateMeetingResponseJson Message" should "be un/marshalled" in {
    val ujrm = JsonParser(CreateMeetingResponseJson)
    val message = ujrm.convertTo[CreateMeetingResponseFormat]
    
    message.payload.session should be (eng101SessionId)

    import spray.json.DefaultJsonProtocol._
    val jsonMessage = message.toJson
    val sessionIdLens = ("payload" /"session")
    val sessionId = jsonMessage.extract[String](sessionIdLens)
    sessionId should be (eng101SessionId)
  }

  "A MeetingCreatedEvent Message" should "be un/marshalled" in {
    val ujrm = JsonParser(MeetingCreatedEventJson)
    val message = ujrm.convertTo[MeetingCreatedEventFormat]
    
    message.payload.session should be (eng101SessionId)

    import spray.json.DefaultJsonProtocol._
    val jsonMessage = message.toJson
    val sessionIdLens = ("payload" /"session")
    val sessionId = jsonMessage.extract[String](sessionIdLens)
    sessionId should be (eng101SessionId)
  }
}