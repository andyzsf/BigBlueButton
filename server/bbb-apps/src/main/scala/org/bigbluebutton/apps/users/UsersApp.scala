package org.bigbluebutton.apps.users

import org.bigbluebutton.apps.utils.RandomStringGenerator
import org.bigbluebutton.apps.models.Role
import Model._

object UsersApp {               
  def apply() = new UsersApp()
}

class UsersApp private {
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
    val userId = generateValidUserId
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
  private def remove(id: String):Option[JoinedUser] = {
    joinedUsers get (id) match {
      case Some(user) => {
        joinedUsers -= id
        Some(user)
      }
      case None => None
    }
  }
  
  /**
   * Returns true if the user is in the meeting.
   * 
   * @param  the user id
   */
  def exist(id: String):Boolean = joinedUsers.get(id) != None
  
  private def generateValidToken: String = {
    val token = RandomStringGenerator.randomAlphanumericString(12)
    if (! isRegistered(token)) token else generateValidToken
  }
  
  private def generateValidUserId: String = {
    val userId = RandomStringGenerator.randomAlphanumericString(6)
    if (! exist(userId)) userId else generateValidUserId
  }
  
  def register(user: User):RegisteredUser = {
    val token = generateValidToken    
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

  def left(id: String):Option[JoinedUser] = {
    remove(id)
  }
  

  
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