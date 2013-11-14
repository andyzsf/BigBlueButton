package org.bigbluebutton.apps.protocol

import spray.json.JsonParser
import spray.json.DefaultJsonProtocol
import CreateMeetingMessage._
import spray.json.JsObject
import spray.json.JsValue
import spray.json.DeserializationException

object CreateMeetingMessage {
  case class MeetingDef(name: String, externalId: String, record: Boolean,
    welcomeMessage: String, logoutUrl: String, avatarUrl: String,
    users: UsersDef, duration: DurationDef, voiceConf: VoiceConfDef,
    phoneNumbers: Seq[PhoneNumber], metadata: Map[String, String])
  
  case class UsersDef(max: Int, hardLimit: Boolean)
  case class DurationDef(length: Int, allowExtend: Boolean, warnBefore: Int)
  case class VoiceConfDef(pin: Int, number: Int)
  case class PhoneNumber(number: String, description: String)  
}

object CreateMeetingJsonProtocol extends DefaultJsonProtocol {
  implicit val usersDefFormat = jsonFormat2(UsersDef)
  implicit val durationDefFormat = jsonFormat3(DurationDef)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConfDef)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumber)
  implicit val meetingDefFormat = jsonFormat11(MeetingDef)
}

import CreateMeetingJsonProtocol._


object CreateMessageHandler {

  def processCreateMeetingMessage(payloadObj: JsObject):Option[MeetingDef] = {
    val meeting = payloadObj.fields.get("meeting")
    
    if (meeting != None) {
      try {
        Some(meeting.get.convertTo[MeetingDef])
      } catch {
        case e: DeserializationException => None
      }
    } else {
      None
    }
  }
}