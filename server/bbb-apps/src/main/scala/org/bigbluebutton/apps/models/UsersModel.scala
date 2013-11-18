package org.bigbluebutton.apps.models

class UsersModel {
  private var registeredUsers = new collection.immutable.HashMap[String, RegisteredUser]
  private var joinedUsers = new collection.immutable.HashMap[String, JoinedUser]
  
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
  def add(user: RegisteredUser) = registeredUsers += user.authToken -> user
  
  /**
   * Joins a user in the meeting.
   */
  def add(user: JoinedUser) = joinedUsers += (user.id -> user)

  
  /**
   * Returns true if the user is in the meeting.
   * 
   * @param  the user id
   */
  def exist(id: String):Boolean = joinedUsers.get(id) != None
  
  /** 
   *  Removes the user from the joined users.
   *
   *  @param  the id of the user that left the meeting.
   *  @return user that left or None if user does not exist.     
   *               
   */
  def remove(id: String) = joinedUsers -= id
  
  /**
   * Returns the registered users of the meeting.
   */
  def registered:Array[RegisteredUser] = registeredUsers.values toArray
  
  /**
   * Returns the joined users.
   */
  def joined:Array[JoinedUser] = joinedUsers.values toArray  
  
  /**
   * Returns the moderators in the meeting.
   */
  def moderators:Array[JoinedUser] = 
        joinedUsers.values filter(p => p.role == Role.MODERATOR) toArray  
  /**
   * Returns the viewers in the meeting.
   */
  def viewers:Array[JoinedUser] = 
        joinedUsers.values filter (p => p.role == Role.VIEWER) toArray
  
  /**
   * Returns the current presenter in the meeting.
   */      
  def currentPresenter:Option[JoinedUser] = 
        joinedUsers.values find (p => p.isPresenter)
  
  def getJoinedUser(id: String):Option[JoinedUser] = joinedUsers.get(id)
  
}