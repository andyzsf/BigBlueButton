package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.DeserializationException
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.models.PhoneNumber
import org.bigbluebutton.apps.models.VoiceConfAndPin
import org.bigbluebutton.apps.models.MeetingDuration
import org.bigbluebutton.apps.models.UsersLimit
import akka.event.LoggingAdapter
import akka.event.slf4j.SLF4JLogging
import org.bigbluebutton.apps.models.MeetingSession

object CreateMeetingRequestJsonProtocol1 extends DefaultJsonProtocol {
  implicit val usersDefFormat = jsonFormat2(UsersLimit)
  implicit val durationDefFormat = jsonFormat3(MeetingDuration)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConfAndPin)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumber)
  implicit val meetingDefFormat = jsonFormat11(MeetingDescriptor)
}

case class CreateMeetingRequestPayload(meeting: MeetingDescriptor, session: MeetingSession)
case class CreateMeetingJsonResponse(header: Header, payload: CreateMeetingRequestPayload)

case class CreateMeetingRequest(header: Header, payload: MeetingDescriptor) extends InMessage

trait MeetingMessageHandler extends SLF4JLogging {
  import CreateMeetingRequestJsonProtocol1._
  
  def handleCreateMeetingRequest(header: Header, 
                                 payload: JsObject):InMessage = {
    payload.fields.get("meeting") match {
      case Some(meeting) => {
	      try {
	        val m = meeting.convertTo[MeetingDescriptor]
	        CreateMeetingRequest(header, m)
	      } catch {
	        case e: DeserializationException => {
	          throw MessageProcessException("Failed to deserialize create meeting message: [" + payload + "]")
	        }
	      }        
      }
      case None => throw MessageProcessException("Malformed message: [" + payload + "]")
    }   
  }
}