package org.bigbluebutton.meeting

import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models._

object CreateMeetingRequestJsonProtocol extends DefaultJsonProtocol {
  import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._

  implicit val durationFormat = jsonFormat3(Duration)
  implicit val voiceConfDefFormat = jsonFormat2(VoiceConference)
  implicit val phoneNumberDefFormat = jsonFormat2(PhoneNumber)
  implicit val meetingDefFormat = jsonFormat9(Meeting)
  
  implicit val createMeetingRequestPayloadFormat = jsonFormat1(CreateMeetingRequestPayload)
  implicit val createMeetingResponsePayloadFormat = jsonFormat3(CreateMeetingResponsePayload)
  implicit val createMeetingJsonResponseFormat = jsonFormat2(CreateMeetingJsonResponse)
  implicit val createMeetingRequestMessageFormat = jsonFormat2(CreateMeetingRequestMessage)
}


case class CreateMeetingRequestPayload(meeting: Meeting)
	                                       
case class CreateMeetingResponsePayload(success: Boolean, message: String,
                                        meeting: Meeting)
                                          
case class CreateMeetingJsonResponse(header: Header, 
                                     payload: CreateMeetingResponsePayload)
	
case class CreateMeetingRequestMessage(header: Header, 
                                       payload: CreateMeetingRequestPayload) 
	
case class CreateMeetingRequest(header: Header, payload: Meeting)  


case class Meeting(record: Boolean, welcome_message: String, 
                   logout_url: String, avatar_url: String,
                   max_users: Int, duration: Duration, 
                   voice_conference: VoiceConference, 
                   phone_numbers: Seq[PhoneNumber], 
                   metadata: Map[String, String])
case class Duration(length: Int, allow_extend: Boolean, max: Int)

