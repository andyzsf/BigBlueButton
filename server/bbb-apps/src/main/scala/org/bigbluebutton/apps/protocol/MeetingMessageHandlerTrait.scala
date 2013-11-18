package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import org.bigbluebutton.apps.models.Core._
import spray.json.DeserializationException
import spray.json.DefaultJsonProtocol

object CreateMeetingRequestJsonProtocol extends DefaultJsonProtocol {
  implicit val usersDefFormat = jsonFormat2(UsersSpec)
  implicit val durationDefFormat = jsonFormat3(DurationSpec)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConfSpec)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumberSpec)
  implicit val meetingDefFormat = jsonFormat11(MeetingSpec)
}

trait MeetingMessageHandlerTrait {
  import CreateMeetingRequestJsonProtocol._
  
  def extractCreateMeetingPayload(payloadObj: JsObject):Option[MeetingSpec] = {
    val meeting = payloadObj.fields.get("meeting")
    
    if (meeting != None) {
      try {
        Some(meeting.get.convertTo[MeetingSpec])
      } catch {
        case e: DeserializationException => None
      }
    } else {
      None
    }
  }
}