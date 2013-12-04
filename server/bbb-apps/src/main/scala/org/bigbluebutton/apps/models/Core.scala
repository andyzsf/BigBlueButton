package org.bigbluebutton.apps.models



object Role extends Enumeration {
	type RoleType = Value
	val MODERATOR = Value("MODERATOR")
	val VIEWER = Value("VIEWER")
}

  
case class MeetingConfig(name: String, externalId: String, 
                       record: Boolean, welcomeMessage: String, 
                       logoutUrl: String, avatarUrl: String,
                       users: UsersConfig, duration: DurationConfig, 
                       voiceConf: VoiceConfig, phoneNumbers: Seq[PhoneNumberConfig], 
                       metadata: Map[String, String])
  
case class UsersConfig(max: Int, hardLimit: Boolean)
case class DurationConfig(length: Int, allowExtend: Boolean, warnBefore: Int)
case class VoiceConfig(pin: Int, number: Int)
case class PhoneNumberConfig(number: String, description: String)  
case class MeetingSession(name: String, externalId: String, session: String)


