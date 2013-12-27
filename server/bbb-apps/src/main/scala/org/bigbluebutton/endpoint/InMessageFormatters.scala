package org.bigbluebutton.endpoint

import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.models.MeetingIdAndName
import org.bigbluebutton.apps.users.data.UserIdAndName

sealed abstract class InMsgFormatter

case class CreateMeetingRequestMessage(header: Header,
                  payload: CreateMeetingRequestPayload) extends InMsgFormatter

case class CreateMeetingResponseFormat(header: Header,
                  payload: CreateMeetingResponsePayload) extends InMsgFormatter
                  
case class RegisterUserRequestFormat(header: Header, 
                  payload: RegisterUserRequestPayloadFormat) extends InMsgFormatter

case class UserJoinRequestMessage(header: Header, 
                  payload: UserJoinRequestPayload) extends InMsgFormatter                                  

case class UserLeaveMessage(header: Header, 
                  payload: UserLeavePayload) extends InMsgFormatter

case class GetUsersRequestMessage(header: Header, 
                  payload: GetUsersRequestPayload) extends InMsgFormatter
                           
case class AssignPresenterMessage(header: Header, 
                  payload: AssignPresenterPayload) extends InMsgFormatter
