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
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.protocol.HeaderEvent
import org.bigbluebutton.apps.protocol.HeaderMeeting
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import spray.json.JsString
import org.bigbluebutton.apps.protocol.MessageTransformer
import akka.event.LoggingAdapter
import akka.actor.ActorLogging
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import org.bigbluebutton.apps.protocol.CreateMeetingRequestReply
import scala.util.{Success, Failure}


class RestEndpointServiceActor(val msgReceiver: ActorRef) extends Actor with RestEndpointService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}


trait RestEndpointService extends HttpService {
  import MessageTransformer._
  import org.bigbluebutton.apps.protocol.CreateMeetingRequestReplyJsonProtocol._
  import org.bigbluebutton.apps.protocol.HeaderAndPayloadJsonSupport._
  
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
	        entity(as[HeaderAndPayload]) { hp =>
              val response = sendCreateMeetingMessage(hp)
              complete("{OK}")
	        }
        }
      }
    }
    
    def sendCreateMeetingMessage(hp: HeaderAndPayload):Boolean = {
	  MessageTransformer.processMessage(hp.header, hp.payload.asJsObject) match {
	    case Success(message) => {
	       val response = (msgReceiver ? message)
	                      .mapTo[CreateMeetingRequestReply]
	                      .map(result => {
	                        result})
	                      
           true
	    }
	    case Failure(ex) => {
	      println(s"Problem parsing message content: ${ex.getMessage}")
	      false
	    }
	  }      
    }
}


