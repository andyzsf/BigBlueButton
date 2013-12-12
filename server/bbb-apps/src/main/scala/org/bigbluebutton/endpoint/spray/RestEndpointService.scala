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
import org.bigbluebutton.apps.protocol.Protocol._
import spray.json.JsString
import org.bigbluebutton.apps.protocol.MessageTransformer
import akka.event.LoggingAdapter
import akka.actor.ActorLogging
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import scala.util.{Success, Failure}
import org.bigbluebutton.apps.protocol.HeaderBuilder
import org.bigbluebutton.apps.protocol.StatusCodeBuilder
import org.bigbluebutton.apps.protocol.StatusCodes
import org.bigbluebutton.apps.protocol.ErrorCodeBuilder
import org.bigbluebutton.apps.protocol.ErrorCodes
import org.bigbluebutton.apps.MeetingMessage.{CreateMeeting, CreateMeetingResponse}
import org.bigbluebutton.apps.protocol.MeetingMessages._


class RestEndpointServiceActor(val msgReceiver: ActorRef) extends Actor with RestEndpointService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}


trait RestEndpointService extends HttpService {
  import MessageTransformer._
  import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._
  import org.bigbluebutton.apps.protocol.CreateMeetingRequestJsonProtocol1._
  
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
    
    def buildJsonResponse(message: CreateMeetingRequestMessage, result: CreateMeetingResponse):CreateMeetingJsonResponse = {
      	  var statusCode = StatusCodeBuilder.buildStatus(StatusCodes.OK)
	      val payload = CreateMeetingResponsePayload(result.success, result.message,
	                                 message.payload.meeting, result.session)

	      val response = Response(statusCode)
		  val headerEvent = HeaderBuilder.buildResponseHeader("CreateMeetingResponse", message.header.event)
		  val header = message.header.copy(response = Some(response), event = headerEvent)
		  CreateMeetingJsonResponse(header, payload)       
    }
    
    def buildJsonFailedResponse(message: CreateMeetingRequestMessage):CreateMeetingJsonResponse = {
	      val statusCode = StatusCodeBuilder.buildStatus(StatusCodes.BAD_REQUEST)
	      val payload = CreateMeetingResponsePayload(false, "Some exception was thrown", message.payload.meeting, None)
		  val response = Response(statusCode)
	      val headerEvent = HeaderBuilder.buildResponseHeader("CreateMeetingResponse", message.header.event)
	      val header = message.header.copy(response = Some(response), event = headerEvent)
		   CreateMeetingJsonResponse(header, payload)   
    }
    
    def sendCreateMeetingMessage(message: CreateMeetingRequestMessage) = {   
      val createMeetingMessage = CreateMeeting(message.payload.meeting)
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


