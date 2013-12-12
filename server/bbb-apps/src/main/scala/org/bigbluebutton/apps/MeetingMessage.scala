package org.bigbluebutton.apps

import org.bigbluebutton.apps.models.{Session, MeetingDescriptor}

object MeetingMessage {
  case class CreateMeeting(descriptor: MeetingDescriptor)
  case class CreateMeetingResponse(success: Boolean, 
                                   descriptor: MeetingDescriptor, 
                                   message: String, 
                                   session: Option[Session])
                                   
  case class MeetingCreated(session: Session, meeting: MeetingDescriptor)
}