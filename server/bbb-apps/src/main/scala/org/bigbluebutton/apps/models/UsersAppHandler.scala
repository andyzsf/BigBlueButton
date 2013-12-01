package org.bigbluebutton.apps.models

import akka.actor.ActorRef
import org.bigbluebutton.apps.utils.RandomStringGenerator
import org.bigbluebutton.apps.protocol.UserRegistered
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.protocol.RegisterUserRequest
import org.bigbluebutton.apps.MeetingActor
import org.bigbluebutton.apps.models.UsersApp.RegisteredUser

/**
 * Users app for meeting
 */
trait UsersAppHandler {  
  this : MeetingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  val usersApp = new UsersApp
  
  def handleRegisterUser(msg: RegisterUserRequest) = {
    val token = usersApp.getValidToken
    val internalId = usersApp.getValidUserId    
    val ruser = msg.payload
    val ru = RegisteredUser(token, internalId, ruser)
    
    
  }
}