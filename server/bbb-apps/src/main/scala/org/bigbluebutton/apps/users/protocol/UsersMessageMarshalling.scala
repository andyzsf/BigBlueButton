package org.bigbluebutton.apps.users.protocol

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import spray.json._
import spray.json.DefaultJsonProtocol._
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.endpoint.MessageMarshallingActor
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.users.messages.UserJoinResponse
import org.bigbluebutton.endpoint.redis.JsonMessage
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.endpoint.UserMessagesProtocol
import org.bigbluebutton.endpoint.UserJoinResponseMessage
import org.bigbluebutton.endpoint.UserJoinResponseJsonPayload
import org.bigbluebutton.endpoint.UserJoinResponseJsonMessage
import org.bigbluebutton.endpoint.UserFormat
import org.bigbluebutton.endpoint.ResultFormat


trait UsersMessageMarshalling {
  this : MessageMarshallingActor =>
  
  val pubsubActor: ActorRef
  val log: LoggingAdapter
  
  private def publishUserJoinResponse(header: Header, payload: UserJoinResponseJsonPayload) = {
    import UserMessagesProtocol._
    
    val jsonFormatMsg = UserJoinResponseJsonMessage(header, payload).toJson 
    val jsonMsg = JsonMessage(header.destination.to, jsonFormatMsg.compactPrint)
    pubsubActor ! jsonMsg     
  }
  
  def marshallUserJoinResponseMessage(msg: UserJoinResponseMessage) = {    
    if (msg.response.result.success) {
      msg.response.user match {
        case Some(usr) => {
          val user = UserFormat(usr.id, usr.user.externalId, usr.user.name, 
	            usr.user.role, usr.user.pin, usr.user.welcomeMessage,
	            usr.user.logoutUrl, usr.user.avatarUrl)
	      val result = ResultFormat(msg.response.result.success,
	                   msg.response.result.message)
	      val payload = UserJoinResponseJsonPayload(
	                      msg.response.session.meeting, 
	                      msg.response.session.id,
	                      result, Some(user))	
	      publishUserJoinResponse(msg.header, payload)
        }
        case None => log.error("Empty user for UserJoinResponse message.")
      }             
    } else {
      val result = ResultFormat(false,
	                   "Failed to get response.")
      val payload = UserJoinResponseJsonPayload(
                      msg.response.session.meeting, msg.response.session.id,
                      result, None)
      publishUserJoinResponse(msg.header, payload) 
    }
 
  }
}