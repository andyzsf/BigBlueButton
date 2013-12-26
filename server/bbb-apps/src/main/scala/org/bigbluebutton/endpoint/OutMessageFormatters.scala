package org.bigbluebutton.endpoint

import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.models.MeetingIdAndName

sealed abstract class OutMsgFormatter

case class UserJoinResponseJsonMessage(header: Header, 
                 payload: UserJoinResponseJsonPayload) extends OutMsgFormatter
case class UserJoinResponseJsonPayload(meeting: MeetingIdAndName, 
                       session: String, result: ResultFormat, 
                       user: Option[UserFormat]) 