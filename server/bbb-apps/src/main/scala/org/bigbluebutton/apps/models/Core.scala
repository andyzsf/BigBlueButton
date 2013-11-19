package org.bigbluebutton.apps.models

object Role extends Enumeration {
	type Role = Value
	val MODERATOR = Value("MODERATOR")
	val VIEWER = Value("VIEWER")
}

import Role._
  
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
  
case class WebIdentity(name: String)
case class CallerId(name: String, number: String)
case class VoiceIdentity(name: String, callerId: CallerId)

case class MeetingSession(name: String, externalId: String, session: String)
	
case class JoinedUser(id: String, role: Role, isPresenter: Boolean = false, 
                      webIdent: Option[WebIdentity] = None, 
                      voiceIdent: Option[VoiceIdentity] = None)
	
case class RegisteredUser(authToken: String, name: String)

case class UserIdAndName(id: String, name: String)
  
object SystemUser extends UserIdAndName(id = "system", name = "System")
