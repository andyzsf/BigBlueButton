package org.bigbluebutton.apps.models

import akka.actor.ActorRef
import org.bigbluebutton.apps.utils.RandomStringGenerator
import org.bigbluebutton.apps.protocol.UserRegistered
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.MeetingActor
import org.bigbluebutton.apps.models.UsersApp.RegisteredUser
import org.bigbluebutton.apps.protocol.UserMessages._
import org.bigbluebutton.apps.protocol.Response
import org.bigbluebutton.apps.protocol.StatusCodes
import org.bigbluebutton.apps.protocol.ErrorCodes
import org.bigbluebutton.apps.protocol.{StatusCode, ErrorCode}


/**
 * Users app for meeting
 */
trait UsersAppHandler {  
  this : MeetingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter
  val usersApp = UsersApp()
  
  def handleRegisterUser(msg: RegisterUserRequest) = {
    val user = usersApp.register(msg.payload)
    
  }
  
  def handleJoinUser(msg: JoinUserRequest) = {
    val token = msg.token
    usersApp.join(token) match {
      case Some(juser) => {
        val response = Response(status = StatusCode(StatusCodes.OK.id, StatusCodes.OK.toString()))
        val jur = JoinUserResponse(response, token, Some(juser))
        sender ! jur
      }
      case None => {
        val statusCode = StatusCode(StatusCodes.NOT_FOUND.id,
                                    StatusCodes.NOT_FOUND.toString())
        val errorCode = ErrorCode(ErrorCodes.INVALID_TOKEN.id,
                                  ErrorCodes.INVALID_TOKEN.toString())
        val response = Response(status = statusCode,
                        errors = Some(Seq(errorCode)))
        val jur = JoinUserResponse(response, token, None)
        sender ! jur        
      }
    }
  }
}