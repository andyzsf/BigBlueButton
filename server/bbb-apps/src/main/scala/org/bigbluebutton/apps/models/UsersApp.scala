package org.bigbluebutton.apps.models

import org.bigbluebutton.apps.utils.RandomStringGenerator

object UsersApp {
  case class WebIdentity(name: String)
  case class CallerId(name: String, number: String)
  case class VoiceIdentity(name: String, callerId: CallerId)
  case class UserIdAndName(id: String, name: String)
  object SystemUser extends UserIdAndName(id = "system", name = "System")
  
  case class JoinedUser(id: String, token: String, 
		  				isPresenter: Boolean = false, 
		  				user: User,
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
  
  private val usersModel = new UsersModel  
  private var presenterAssignedBy = SystemUser
  
  def registered:Array[RegisteredUser] = usersModel.registered
  def joined:Array[JoinedUser] = usersModel.joined
  
  def getValidToken: String = {
    val token = RandomStringGenerator.randomAlphanumericString(12)
    if (! usersModel.isRegistered(token)) token else getValidToken
  }
  
  def getValidUserId: String = {
    val userId = RandomStringGenerator.randomAlphanumericString(6)
    if (! usersModel.exist(userId)) userId else getValidUserId
  }
  
  def register(registeredUser: RegisteredUser) = {
    val token = getValidToken    
    val user = registeredUser.copy(token = token)
    usersModel.add(user)
  }
  
  def join(user: JoinedUser) = {
    val userId = getValidUserId
    usersModel.add(user.copy(id = userId))
    
  }
  


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