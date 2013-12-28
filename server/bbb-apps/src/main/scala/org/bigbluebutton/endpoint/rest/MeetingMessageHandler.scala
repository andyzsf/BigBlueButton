package org.bigbluebutton.endpoint.rest

import akka.actor.{Actor, Props, ActorRef, ActorLogging}
import akka.event.LoggingAdapter
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import org.bigbluebutton.apps.CreateMeetingResponse
import org.bigbluebutton.meeting._
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps._
import org.bigbluebutton.apps._
import scala.concurrent.Future
import org.bigbluebutton.endpoint._


trait MeetingMessageHandler {
  this : RestEndpointServiceActor =>
  
  val msgReceiver: ActorRef

  def buildJsonResponse(message: CreateMeetingRequestMessage, 
                        response: CreateMeetingResponse):
                        CreateMeetingResponseFormat = {

    val desc = response.descriptor
    
    val duration = DurationFormat(desc.duration.lengthInMinutes, 
                       desc.duration.allowExtend, desc.duration.maxDuration)
    val vc = VoiceConferenceFormat(desc.voiceConf.pin, desc.voiceConf.number)    
    
    val pns = desc.phoneNumbers map { 
       pn => PhoneNumberFormat(pn.number, pn.description) 
    }        
    
    val descriptor = MeetingDescriptorFormat(desc.name, desc.id, desc.record,
                       desc.welcomeMessage, desc.logoutUrl, desc.avatarUrl,
                       desc.numUsers, duration, vc, pns, desc.metadata)
    val meeting = MeetingIdAndName(message.payload.meeting.id, message.payload.meeting.name)
	val payload = CreateMeetingResponsePayloadFormat(meeting, 
	                   response.session.id, descriptor)
    CreateMeetingResponseFormat(message.header, payload)       
  }
    
    def buildJsonFailedResponse(message: CreateMeetingRequestMessage):CreateMeetingResponseFormat = {
	      val meeting = MeetingIdAndName(message.payload.meeting.id, message.payload.meeting.name)
	      val payload = CreateMeetingResponsePayloadFormat(meeting, "Some exception was thrown", message.payload.meeting.)

		   CreateMeetingResponseFormat(message.header, payload)   
    }
    
    def sendCreateMeetingMessage(message: CreateMeetingRequestFormat):Future[CreateMeetingResponseFormat] = {   
      val meetingDuration = 
               MeetingDuration(message.payload.meeting.duration.length,
                               message.payload.meeting.duration.allow_extend,
                               message.payload.meeting.duration.max)
      val voiceConf = 
               VoiceConference(message.payload.meeting.voice_conference.pin,
                               message.payload.meeting.voice_conference.number)
      
      val mdesc = MeetingDescriptor(message.payload.meeting.id, 
                                    message.payload.meeting.name,
                                    message.payload.meeting.record, 
                                    message.payload.meeting.welcome_message, 
                                    message.payload.meeting.logout_url, 
                                    message.payload.meeting.avatar_url,
                                    message.payload.meeting.max_users,
                                    meetingDuration, 
                                    voiceConf, 
                                    message.payload.meeting.phone_numbers,
                                    message.payload.meeting.metadata)
                                    
      val createMeetingMessage = CreateMeeting(mdesc)
	  val response = (msgReceiver ? createMeetingMessage)
	                 .mapTo[CreateMeetingResponse]
	                 .map(result => {
                        buildJsonResponse(message, result)                  
	                  })
	                  .recover { case _ =>
                         buildJsonFailedResponse(message)
	                  }
	  
	  response

    }
}