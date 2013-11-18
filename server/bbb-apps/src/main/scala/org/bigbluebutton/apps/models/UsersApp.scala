package org.bigbluebutton.apps.models

import akka.actor.ActorRef
import org.bigbluebutton.apps.utils.RandomStringGenerator

/**
 * Users app for meeting
 */
trait Users {  
  val pubSub: ActorRef
  
  private val usersModel = new UsersModel
  
  private var presenterAssignedBy = SystemUser
  
  def getValidToken: String = {
    val token = RandomStringGenerator.randomAlphanumericString(12)
    if (! usersModel.isRegistered(token)) token else getValidToken
  }
  
  def register(user: RegisteredUser) = {
    val token = getValidToken
    
    usersModel.add(user)
  }
  
//  def join(user: JoinedUser) = joinedUsers += (user.id -> user)
  

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