package org.bigbluebutton.endpoint.rest

import akka.actor.{Actor, Props, ActorRef, ActorLogging}
import akka.event.LoggingAdapter
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import org.bigbluebutton.apps.CreateMeetingResponse
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps._
import scala.concurrent.Future
import org.bigbluebutton.endpoint._
import org.bigbluebutton.SystemConfiguration


trait MeetingMessageHandler extends SystemConfiguration {
  this : RestEndpointServiceActor =>
  
  val msgReceiver: ActorRef
   
  def sendCreateMeetingMessage(message: CreateMeetingRequestFormat):
               Future[CreateMeetingResponseFormat] = {
    
    val meeting = MeetingIdAndName(message.payload.meeting_descriptor.external_id,
                           message.payload.meeting_descriptor.name)
    val descriptor = message.payload.meeting_descriptor  

    val replyDestination = message.header.reply
    

	val header = Header(Destination(apiChannel, None), 
	                      InMsgNameConst.CreateMeetingResponse, 
                          Util.generateTimestamp, apiSourceName, None)
    
    
    def buildJsonFailedResponse():CreateMeetingResponseFormat = {
      val result = ResultFormat(false, "Failed to get a response.")
      
	  val payload = CreateMeetingResponsePayloadFormat(meeting, None, 
	                      result, descriptor)

	  CreateMeetingResponseFormat(header, payload)   
    }
    
    def buildJsonResponse(response: CreateMeetingResponse):
                        CreateMeetingResponseFormat = {
      val result = ResultFormat(response.success, response.message)
      
	  val payload = CreateMeetingResponsePayloadFormat(meeting, 
	                   Some(response.session.id), result, descriptor)
      CreateMeetingResponseFormat(header, payload)       
    }
        
    val duration = Duration(descriptor.duration.length,
                            descriptor.duration.allow_extend,
                            descriptor.duration.max)
                            
    val voiceConf = VoiceConference(descriptor.voice_conference.pin,
                            descriptor.voice_conference.number)
    
    val phoneNumbers = descriptor.phone_numbers map { pn =>
      PhoneNumber(pn.number, pn.description)
    }
    
    val mdesc = MeetingDescriptor(meeting.id, 
                                  meeting.name,
                                  descriptor.record, 
                                  descriptor.welcome_message, 
                                  descriptor.logout_url, 
                                  descriptor.avatar_url,
                                  descriptor.max_users,
                                  duration, 
                                  voiceConf, 
                                  phoneNumbers,
                                  descriptor.metadata)
                                    
      val createMeetingMessage = CreateMeeting(mdesc)
	  val response = (msgReceiver ? createMeetingMessage)
	                 .mapTo[CreateMeetingResponse]
	                 .map(result => {
                        buildJsonResponse(result)                  
	                  })
	                  .recover { case _ =>
                         buildJsonFailedResponse()
	                  }
	  
	  response

    }
}