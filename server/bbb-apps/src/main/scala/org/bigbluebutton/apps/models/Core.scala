package org.bigbluebutton.apps.models

object Core {
  case class MeetingSpec(name: String, externalId: String, record: Boolean,
    welcomeMessage: String, logoutUrl: String, avatarUrl: String,
    users: UsersSpec, duration: DurationSpec, voiceConf: VoiceConfSpec,
    phoneNumbers: Seq[PhoneNumberSpec], metadata: Map[String, String])
  
  case class UsersSpec(max: Int, hardLimit: Boolean)
  case class DurationSpec(length: Int, allowExtend: Boolean, warnBefore: Int)
  case class VoiceConfSpec(pin: Int, number: Int)
  case class PhoneNumberSpec(number: String, description: String)  
}