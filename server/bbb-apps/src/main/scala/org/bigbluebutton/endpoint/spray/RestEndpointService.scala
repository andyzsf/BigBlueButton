package org.bigbluebutton

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.routing.directives.BasicDirectives._
import spray.routing.Directive.pimpApply
import shapeless._
import spray.json.DefaultJsonProtocol
import akka.actor.ActorRef
import spray.json.JsObject
import spray.httpx.SprayJsonSupport
import spray.json.JsValue
import akka.actor.Props
import spray.json.JsString
import org.bigbluebutton.apps.protocol.MessageTransformer
import akka.event.LoggingAdapter
import akka.actor.ActorLogging
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import scala.util.{Success, Failure}
import org.bigbluebutton.apps.protocol._
import org.bigbluebutton.endpoint.spray.MeetingMessageHandler
import org.bigbluebutton.apps.models._
import org.bigbluebutton.meeting.CreateMeetingRequestMessage
import org.bigbluebutton.apps._

class RestEndpointServiceActor(val msgReceiver: ActorRef) extends Actor with RestEndpointService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}


trait RestEndpointService extends HttpService with MeetingMessageHandler {
  import MessageTransformer._
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
      
      val mdesc = MeetingDescriptor(message.header.meeting.id, 
                                    message.header.meeting.name,
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


