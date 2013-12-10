package org.bigbluebutton.apps.models



object Role extends Enumeration {
	type RoleType = Value
	val MODERATOR = Value("MODERATOR")
	val VIEWER = Value("VIEWER")
}

  
case class MeetingDescriptor(name: String, externalId: String, 
                       record: Boolean, welcomeMessage: String, 
                       logoutUrl: String, avatarUrl: String,
                       users: UsersLimit, duration: MeetingDuration, 
                       voiceConf: VoiceConfAndPin, phoneNumbers: Seq[PhoneNumber], 
                       metadata: Map[String, String])
  
case class UsersLimit(max: Int, hardLimit: Boolean)
case class MeetingDuration(length: Int, allowExtend: Boolean, warnBefore: Int)
case class VoiceConfAndPin(pin: Int, number: Int)
case class PhoneNumber(number: String, description: String)  
case class MeetingSession(name: String, externalId: String, session: String)


