package org.bigbluebutton.endpoint.redis

import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.AppsTestFixtures
import org.bigbluebutton.endpoint.UserJoinRequestMessage
import org.bigbluebutton.endpoint.UserJoinRequestPayload
import org.bigbluebutton.apps.protocol.Destination
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.users.messages.UserJoinResponse
import org.bigbluebutton.apps.users.messages.Result
import org.bigbluebutton.apps.protocol.ReplyDestination
import org.bigbluebutton.endpoint.UserJoinResponseMessage
import org.bigbluebutton.endpoint.UserFormat
import org.bigbluebutton.endpoint.UserJoinResponseFormatPayload
import org.bigbluebutton.endpoint.InMsgNameConst
import org.bigbluebutton.endpoint.ResultFormat


trait UsersMessageTestFixtures extends AppsTestFixtures {
  val userJoinSuccessResponse = UserJoinResponse(eng101Session, Result(true, "Success"),
                              Some(joinedUserJuan))
  val userJoinFailResponse = UserJoinResponse(eng101Session, Result(false, "Success"), None)
                              
  val userJoinRequestPayload = UserJoinRequestPayload(eng101MeetingIdAndName, eng101SessionId, juanUserToken)
                                  
  val destination = Destination("apps_channel", None)
  val replyTo = ReplyDestination("apps_channel", "abc-corelid")
  val userJoinReqHeader = Header(destination, InMsgNameConst.UserJoinRequest, 
                  "2013-12-23T08:50Z", "web-api",
                  Some(replyTo))
  val userJoinRequestMessage = UserJoinRequestMessage(userJoinReqHeader, userJoinRequestPayload)

  val userJoinRespHeader = Header(Destination(replyTo.to,
                           Some(replyTo.correlation_id)), 
                           InMsgNameConst.UserJoinResponse,
                           "2013-12-23T08:50Z", "web-api", None)
  val userJoinResponseMessage = UserJoinResponseMessage(userJoinRespHeader, userJoinSuccessResponse)

  val userFormat = UserFormat(juanUserId, juanUser.externalId, juanUser.name, 
	            juanUser.role, juanUser.pin, juanUser.welcomeMessage,
	            juanUser.logoutUrl, juanUser.avatarUrl)
  val userJoinResponseJsonPayload = UserJoinResponseFormatPayload(
	                      eng101MeetingIdAndName, 
	                      eng101SessionId,
	                      ResultFormat(true, "Success"), Some(userFormat))
	                      
}