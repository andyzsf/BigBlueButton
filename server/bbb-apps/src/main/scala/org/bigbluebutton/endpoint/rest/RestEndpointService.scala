package org.bigbluebutton.endpoint.rest

import akka.actor.{Actor, Props, ActorRef, ActorLogging}
import akka.event.LoggingAdapter
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.util.{Success, Failure}
import spray.json.{JsObject, JsValue, JsString, DefaultJsonProtocol}
import spray.httpx.SprayJsonSupport
import spray.routing._
import spray.http._
import MediaTypes._
import spray.routing.directives.BasicDirectives._
import spray.routing.Directive.pimpApply
import shapeless._
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.apps.models._
import org.bigbluebutton.meeting.CreateMeetingRequestMessage
import org.bigbluebutton.apps._

class RestEndpointServiceActor(val msgReceiver: ActorRef) extends Actor 
         with RestEndpointService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}


trait RestEndpointService extends HttpService with MeetingMessageHandler {
  import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._
  import org.bigbluebutton.meeting.CreateMeetingRequestJsonProtocol._
  
  val msgReceiver: ActorRef
  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(5 seconds)
  
  val supportedContentTypes = List[ContentType](`application/json`, `text/plain`)
  
  val restApiRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { 
          complete {
            <html>
              <body>
                <h1>Welcome to BigBlueButton!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("meeting") {
      post {
        respondWithMediaType(`application/json`) {
	        entity(as[CreateMeetingRequestMessage]) { message => 
              sendCreateMeetingMessage(message)
	        }
        }
      }
    }
        
    def sendCreateMeetingMessage(message: CreateMeetingRequestMessage) = {   
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
	  
	  complete(response)

    }
  
}


