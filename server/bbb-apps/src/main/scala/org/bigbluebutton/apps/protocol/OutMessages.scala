package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.models.JoinedUser
import org.bigbluebutton.apps.models.UserIdAndName
import org.bigbluebutton.apps.models.RegisteredUser
import org.bigbluebutton.apps.models.MeetingSession
import org.bigbluebutton.apps.models.MeetingConfig
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.UsersConfig
import org.bigbluebutton.apps.models.DurationConfig
import org.bigbluebutton.apps.models.VoiceConfig
import org.bigbluebutton.apps.models.PhoneNumberConfig
import org.bigbluebutton.apps.models.MeetingConfig

    object MyJsonProtocol extends DefaultJsonProtocol {
      implicit val sessionFormat = jsonFormat3(MeetingSession)
      implicit val usersDefFormat = jsonFormat2(UsersConfig)
      implicit val durationDefFormat = jsonFormat3(DurationConfig)
      implicit val voiceConfDefFormat = jsonFormat2(VoiceConfig)
      implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumberConfig)
      implicit val meetingDefFormat = jsonFormat11(MeetingConfig)
      implicit val createMeetingRequestReplyFormat = jsonFormat2(CreateMeetingRequestReply)
    }

case class CreateMeetingRequestReply(session: MeetingSession, meeting: MeetingConfig)
case class MeetingCreated(session: MeetingSession, meeting: MeetingConfig)

case class MeetingHeader(name: String, externalId: String, session: String)

case class UserRegistered(meeting: MeetingHeader, user: RegisteredUser)
case class EndAndKickAll(meeting: MeetingHeader) 
case class GetUsersReply(meeting: MeetingHeader, users: Seq[JoinedUser]) 
case class PresenterAssigned(meeting: MeetingHeader, newPresenter: UserIdAndName, assignedBy: UserIdAndName) 
case class UserJoined(meeting: MeetingHeader, user: JoinedUser) 
case class UserLeft(meeting: MeetingHeader, user: UserIdAndName) 
case class UserStatusChange(meeting: MeetingHeader, user: UserIdAndName, status: String, value: Object) 