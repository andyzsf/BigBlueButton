package org.bigbluebutton.apps.users

import akka.actor.{ActorRef, actorRef2Scala}
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.RunningMeetingActor
import org.bigbluebutton.apps.users.Messages._
import org.bigbluebutton.apps.protocol.StatusCodes
import org.bigbluebutton.apps.protocol.ErrorCodes
import org.bigbluebutton.apps.users.Messages.{UserJoinResponse, GetUsersResponse}


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
 
  def handleUserJoinRequest(msg: UserJoinRequest) = {
    val token = msg.token
    usersApp.join(token) match {
      case Some(juser) => {
        val response = Response(true, "Successfully joined the user.")
        val jur = UserJoinResponse(msg.session, response, token, Some(juser))
        sender ! jur
        
        pubsub ! UserJoined(msg.session, token, juser)       
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
}