package org.bigbluebutton.apps.protocol

import spray.json.JsObject
import spray.json.DeserializationException
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.models.PhoneNumber
import org.bigbluebutton.apps.models.VoiceConfAndPin
import org.bigbluebutton.apps.models.MeetingDuration
import org.bigbluebutton.apps.models.MaxUsers
import akka.event.LoggingAdapter
import akka.event.slf4j.SLF4JLogging
import org.bigbluebutton.apps.models.Session
import spray.httpx.SprayJsonSupport
import org.bigbluebutton.apps.MeetingMessage.CreateMeetingResponse

import Protocol._

object CreateMeetingRequestJsonProtocol1 extends DefaultJsonProtocol {
  import HeaderAndPayloadJsonSupport._
  import MeetingMessages._
  
  implicit val usersDefFormat = jsonFormat2(MaxUsers)
  implicit val durationDefFormat = jsonFormat3(MeetingDuration)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConfAndPin)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumber)
  implicit val meetingDefFormat = jsonFormat11(MeetingDescriptor)
  implicit val sessionFormat = jsonFormat3(Session)
  implicit val createMeetingResponseFormat = jsonFormat4(CreateMeetingResponse)
  implicit val createMeetingRequestPayloadFormat = jsonFormat1(CreateMeetingRequestPayload)
  implicit val createMeetingResponsePayloadFormat = jsonFormat4(CreateMeetingResponsePayload)
  implicit val createMeetingJsonResponseFormat = jsonFormat2(CreateMeetingJsonResponse)
  implicit val createMeetingRequestMessageFormat = jsonFormat2(CreateMeetingRequestMessage)
}

object MeetingMessages {
  case class CreateMeetingRequestPayload(meeting: MeetingDescriptor)
	                                       
  case class CreateMeetingResponsePayload(success: Boolean, message: String,
                                          meeting: MeetingDescriptor, 
	                                      session: Option[Session] = None)
  case class CreateMeetingJsonResponse(header: Header, payload: CreateMeetingResponsePayload)
	
  case class CreateMeetingRequestMessage(header: Header, payload: CreateMeetingRequestPayload) 
	
  case class CreateMeetingRequest(header: Header, payload: MeetingDescriptor) extends InMessage  
}


trait MeetingMessageHandler extends SLF4JLogging {
  import MeetingMessages._
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