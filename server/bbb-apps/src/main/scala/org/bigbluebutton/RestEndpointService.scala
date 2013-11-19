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
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import spray.json.JsString
import org.bigbluebutton.apps.protocol.MessageTransformer
import org.bigbluebutton.apps.protocol.HeaderAndPayload
import akka.event.LoggingAdapter
import akka.actor.ActorLogging

class RestEndpointServiceActor(val msgReceiver: ActorRef) extends Actor with RestEndpointService with ActorLogging {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}

object HeaderAndPayloadJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {  
  implicit val headerFormat = jsonFormat4(Header)
  implicit val headerAndPayloadFormats = jsonFormat2(HeaderAndPayload)
}

trait RestEndpointService extends HttpService with MessageTransformer {
  import HeaderAndPayloadJsonSupport._
//  val log: LoggingAdapter
  val msgReceiver: ActorRef
  
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
        entity(as[HeaderAndPayload]) { hp =>
          println(hp)
          val msg = processMessage(hp.header, hp.payload.asJsObject)
          if (msg != None) {
            msgReceiver ! msg.get
          }
          complete(msg.get.toString())
        }
      }
    }
}


