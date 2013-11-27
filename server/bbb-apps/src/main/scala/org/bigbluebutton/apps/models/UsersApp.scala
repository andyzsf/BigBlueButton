package org.bigbluebutton.apps.models

import akka.actor.ActorRef
import org.bigbluebutton.apps.utils.RandomStringGenerator
import org.bigbluebutton.apps.protocol.UserRegistered
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.RegisterUserRequest
import org.bigbluebutton.apps.MeetingActor

/**
 * Users app for meeting
 */
trait UsersApp {  
  this : MeetingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  
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
    val user = registeredUser.copy(authToken = token)
    usersModel.add(user)
  }
  
  def join(user: JoinedUser) = {
    val userId = getValidUserId
    usersModel.add(user.copy(id = userId))
    
    sender 
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