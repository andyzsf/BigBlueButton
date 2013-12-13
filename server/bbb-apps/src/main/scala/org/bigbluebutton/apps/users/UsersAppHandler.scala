package org.bigbluebutton.apps.users

import akka.actor.{ActorRef, actorRef2Scala}
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.RunningMeetingActor
import org.bigbluebutton.apps.users.Messages._
import org.bigbluebutton.apps.protocol.StatusCodes
import org.bigbluebutton.apps.protocol.ErrorCodes


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

  /*
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
  */
}