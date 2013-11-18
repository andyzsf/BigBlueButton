package org.bigbluebutton.apps.models

object Role extends Enumeration {
	type Role = Value
	val MODERATOR = Value("MODERATOR")
	val VIEWER = Value("VIEWER")
}

case class WebIdentity(name: String)
case class CallerId(name: String, number: String)
case class VoiceIdentity(name: String, callerId: CallerId)

import Role._

case class User(id: String, role: Role, isPresenter: Boolean = false, webIdent: Option[WebIdentity] = None, voiceIdent: Option[VoiceIdentity] = None)

case class RegisteredUser(authToken: String, name: String)

trait Users {  
  private var registeredUsers = new collection.immutable.HashMap[String, RegisteredUser]
  private var joinedUsers = new collection.immutable.HashMap[String, User]
  

  def register(user: RegisteredUser) = registeredUsers += user.authToken -> user
  
  def join(user: User):Option[User] = {
    joinedUsers += (user.id -> user)
    joinedUsers.get(user.id)
  }
  
  def hasUser(userId: String):Boolean = joinedUsers.get(userId) != None
  
  def left(userId: String):Option[User] = {
    val u = joinedUsers.get(userId)
    if (u != None) joinedUsers -= userId
    u
  }
  
  def registered:Array[RegisteredUser] = registeredUsers.values toArray
  
  def joined:Array[User] = joinedUsers.values toArray  
  
  def moderators:Array[User] = joinedUsers.values filter(p => p.role == MODERATOR) toArray  
  
  def viewers:Array[User] = joinedUsers.values filter (p => p.role == VIEWER) toArray
  
  def currentPresenter:Option[User] = joinedUsers.values find (p => p.isPresenter)
  
}