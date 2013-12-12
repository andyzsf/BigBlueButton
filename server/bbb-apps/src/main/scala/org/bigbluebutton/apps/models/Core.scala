package org.bigbluebutton.apps.models



object Role extends Enumeration {
	type RoleType = Value
	val MODERATOR = Value("MODERATOR")
	val VIEWER = Value("VIEWER")
}

  
case class MeetingDescriptor(id: String, name: String,  
                       record: Boolean, welcomeMessage: String, 
                       logoutUrl: String, avatarUrl: String,
                       users: MaxUsers, duration: MeetingDuration, 
                       voiceConf: VoiceConfAndPin, phoneNumbers: Seq[PhoneNumber], 
                       metadata: Map[String, String])
  
case class MaxUsers(max: Int, hardLimit: Boolean)
case class MeetingDuration(lengthInMinutes: Int, allowExtend: Boolean, maxDuration: Int)
case class VoiceConfAndPin(pin: Int, number: Int)
case class PhoneNumber(number: String, description: String)  

case class Session(id: String, meetingId: String, meetingName: String)


