package org.bigbluebutton.apps.users

import org.bigbluebutton.apps.models.Role

object Model {
	case class WebIdentity(name: String)
	case class CallerId(name: String, number: String)
	case class VoiceIdentity(name: String, callerId: CallerId)
	case class UserIdAndName(id: String, name: String)
	
	object SystemUser extends UserIdAndName(id = "system", name = "System")
	  
	case class JoinedUser(id: String, token: String, user: User,
	                      isPresenter: Boolean = false,
	                      webIdent: Option[WebIdentity] = None, 
	                      voiceIdent: Option[VoiceIdentity] = None)
		
	case class RegisteredUser(token: String, user: User)
	  
	case class User(externalId: String, name: String, 
	                role: Role.RoleType, pin: Int, welcomeMessage: String,
	                logoutUrl: String, avatarUrl: String)  
}
