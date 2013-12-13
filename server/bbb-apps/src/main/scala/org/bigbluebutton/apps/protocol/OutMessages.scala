package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.users.Model._
import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.models.MeetingDescriptor
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.MaxUsers
import org.bigbluebutton.apps.models.MeetingDuration
import org.bigbluebutton.apps.models.VoiceConfAndPin
import org.bigbluebutton.apps.models.PhoneNumber
import org.bigbluebutton.apps.models.MeetingDescriptor

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val sessionFormat = jsonFormat3(Session)
  implicit val usersDefFormat = jsonFormat2(MaxUsers)
  implicit val durationDefFormat = jsonFormat3(MeetingDuration)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConfAndPin)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumber)
  implicit val meetingDefFormat = jsonFormat11(MeetingDescriptor)
}

case class CreateMeetingRequestReply(created: Boolean, message: String, session: Session)


case class MeetingHeader(name: String, externalId: String, session: String)

case class UserRegistered(meeting: MeetingHeader, user: RegisteredUser)
case class EndAndKickAll(meeting: MeetingHeader) 
case class GetUsersReply(meeting: MeetingHeader, users: Seq[JoinedUser]) 
case class PresenterAssigned(meeting: MeetingHeader, newPresenter: UserIdAndName, assignedBy: UserIdAndName) 
case class UserJoined(meeting: MeetingHeader, user: JoinedUser) 
case class UserLeft(meeting: MeetingHeader, user: UserIdAndName) 
case class UserStatusChange(meeting: MeetingHeader, user: UserIdAndName, status: String, value: Object) 