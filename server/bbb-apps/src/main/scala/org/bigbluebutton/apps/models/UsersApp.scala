package org.bigbluebutton.apps.models

import org.bigbluebutton.apps.utils.RandomStringGenerator

object UsersApp {
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
                
  
  case class UserJoining(meetingID: String, userID: String, name: String, role: String, extUserID: String)
  case class UserLeaving(meetingID: String, userID: String) 
  case class GetUsers(meetingID: String, requesterID: String) 
  case class ChangeUserStatus(meetingID: String, userID: String, status: String, value: Object) 
  case class AssignPresenter(meetingID: String, newPresenterID: String, newPresenterName: String, assignedBy: String)  
  
  
}

class UsersApp {
  import UsersApp._
  
  private var registeredUsers = 
                  new collection.immutable.HashMap[String, RegisteredUser]
  
  private var joinedUsers = 
                  new collection.immutable.HashMap[String, JoinedUser]
  
  private var presenterAssignedBy = SystemUser
  
  /**
   * Returns the registered users of the meeting.
   */
  def registered:Array[RegisteredUser] = registeredUsers.values toArray
  
  /**
   * Returns the joined users.
   */
  def joined:Array[JoinedUser] = joinedUsers.values toArray 
  
  /**
   * Returns true if user with a given token is registered.
   * 
   * @param  the token of the user
   */
  def isRegistered(token: String) = registeredUsers.get(token) != None
  
  def getRegisteredUser(token: String) = registeredUsers.get(token)
  
  /**
   * Registers a user in the meeting.
   * 
   */
  private def add(user: RegisteredUser) = registeredUsers += user.token -> user
  
  /**
   * Joins a user in the meeting.
   */
  private def add(token: String, ruser: User):JoinedUser = {
    val userId = getValidUserId
    val user = JoinedUser(userId, token, ruser)
    joinedUsers += (user.id -> user)
    user
  }
  
  /** 
   *  Removes the user from the joined users.
   *
   *  @param  the id of the user that left the meeting.
   *  @return user that left or None if user does not exist.     
   *               
   */
  private def remove(id: String) = joinedUsers -= id
  
  /**
   * Returns true if the user is in the meeting.
   * 
   * @param  the user id
   */
  def exist(id: String):Boolean = joinedUsers.get(id) != None
  
  private def getValidToken: String = {
    val token = RandomStringGenerator.randomAlphanumericString(12)
    if (! isRegistered(token)) token else getValidToken
  }
  
  private def getValidUserId: String = {
    val userId = RandomStringGenerator.randomAlphanumericString(6)
    if (! exist(userId)) userId else getValidUserId
  }
  
  def register(user: User):RegisteredUser = {
    val token = getValidToken    
    val u = RegisteredUser(token, user)
    add(u)
    u
  }
  
  def join(token: String):Option[JoinedUser] = {    
    for {
      ruser <- getRegisteredUser(token)
	  user = add(token, ruser.user)	        
    } yield user
  }
  
  /**
   * Returns the moderators in the meeting.
   */
  def moderators:Array[JoinedUser] = 
        joinedUsers.values filter(p => p.user.role == Role.MODERATOR) toArray  
        
  /**
   * Returns the viewers in the meeting.
   */
  def viewers:Array[JoinedUser] = 
        joinedUsers.values filter (p => p.user.role == Role.VIEWER) toArray
  
  /**
   * Returns the current presenter in the meeting.
   */      
  def currentPresenter:Option[JoinedUser] = 
        joinedUsers.values find (p => p.isPresenter)
  
  def getJoinedUser(id: String):Option[JoinedUser] = joinedUsers.get(id)

//  def left(id: String):Option[JoinedUser] = {
//    val u = joinedUsers.get(id)
//    if (u != None) joinedUsers -= id
//    u
//  }
  

  
  /**
   * Assign a new presenter in the meeting.
   * 
   * @param  newPresenterId - the user that will become presenter
   *         assignedBy     - the user that assigned the new presenter
   */
//  def assignPresenter(newPresenterId: String, 
//                      assignedBy: UserIdAndName):Option[JoinedUser] = {
//    val curPresenter = currentPresenter
//    val newPresenter = getJoinedUser(newPresenterId)
//    if (curPresenter != None) {
//      val cp = curPresenter.get
//      joinedUsers += (cp.id -> cp.copy(isPresenter = false))
//      curPresenter
//    }
//    
//    None
//  }
}