package org.bigbluebutton.apps.models

case class WebIdentity(name: String)

case class CallerId(name: String, number: String)
case class VoiceIdentity(name: String, callerId: CallerId)

case class User(id: String, role: String, webIdent: Option[WebIdentity] = None, voiceIdent: Option[VoiceIdentity] = None)

case class RegisteredUser(authToken: String, name: String)

trait UsersApp {
  
  var registered = new collection.immutable.HashMap[String, RegisteredUser]
  var users = new collection.immutable.HashMap[String, User]
  
  def register(user: RegisteredUser) = {
    registered += user.authToken -> user
  }
}