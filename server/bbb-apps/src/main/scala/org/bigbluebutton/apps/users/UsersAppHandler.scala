package org.bigbluebutton.apps.users

import akka.actor.{ActorRef, actorRef2Scala}
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.RunningMeetingActor
import org.bigbluebutton.apps.protocol.StatusCodes
import org.bigbluebutton.apps.protocol.ErrorCodes
import org.bigbluebutton.apps.users._
import org.bigbluebutton.apps.users.messages._
import org.bigbluebutton.apps.users.data._


/**
 * Users app for meeting
 */
trait UsersAppHandler {  
  this : RunningMeetingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  val usersApp = UsersApp()
    
  def handleRegisterUser(msg: RegisterUserRequest) = {
    val user = usersApp.register(msg.user)
    val response = Response(true, "User has been registered.")
    sender ! RegisterUserResponse(msg.session, response,
                                  user.token, msg.user)
  }
      
  def handleAssignPresenter(msg: AssignPresenter) = {
    usersApp.getJoinedUser(msg.presenter.presenter.id) match {
      case Some(e) => {
        usersApp.makeAllUsersViewer()
        
        pubsub ! BecomeViewer(msg.session)
        
        usersApp.makePresenter(e)
        pubsub ! BecomePresenter(msg.session, msg.presenter)
      }
      
      case None => log.info("Making an unknown user presenter.")
    }
  }
  
  def handleUserJoinRequest(msg: UserJoinRequest) = {
    val token = msg.token
    usersApp.join(token) match {
      case Some(juser) => {
        val response = Response(true, "Successfully joined the user.")
        val jur = UserJoinResponse(msg.session, response, token, Some(juser))
        sender ! jur
        
        pubsub ! UserJoined(msg.session, token, juser)      
        
        if (juser.user.role == Role.MODERATOR && ! usersApp.hasPresenter) {
          val moderator = usersApp.findAModerator
          moderator foreach { m => 
            usersApp.makePresenter(m)
            val presenter = UserIdAndName(m.id, m.user.name)
            val assignedBy = SystemUser
            val newPresenter = Presenter(presenter, assignedBy)
            pubsub ! BecomePresenter(msg.session, newPresenter)
          }
        }
      }
      case None => {
        val response = Response(false, "Invalid token.")
        val jur = UserJoinResponse(msg.session, response, token, None)
        sender ! jur        
      }
    }
  }
  
  def handleUserLeave(msg: UserLeave) = {
    val user = usersApp.left(msg.userId) 
    user foreach { u =>
      pubsub ! UserLeft(msg.session, u)
    }        
  }
  
  def handleGetUsersRequest(gur: GetUsersRequest) = {
    pubsub ! GetUsersResponse(gur.session, gur.requesterId, usersApp.joined)
  }
  
  def handleRaiseHand(msg: RaiseHand) = {
    
  }
  
  def handleLowerHand(msg: LowerHand) = {
    
  }
  
  def handleVoiceUserJoin(msg: VoiceUserJoin) = {
    val vid = VoiceIdentity(callerId = msg.callerId, muted = msg.muted, 
                            locked = msg.locked, talking = msg.talking,
                            metadata = msg.metadata)
                            
    val joinedUser = usersApp.joinVoiceUser(msg.userId, vid, meeting)
    
    pubsub ! UserJoined(session, joinedUser.token, joinedUser)
  }
}