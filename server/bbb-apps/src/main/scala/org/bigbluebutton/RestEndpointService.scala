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
import org.bigbluebutton.apps.protocol.Header1

class RestEndpointServiceActor(msgReceiver: ActorRef) extends Actor with RestEndpointService {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}

case class Foo1(header: Header1, payload: JsValue)
//case class Header1(name: String, timestamp: Long, correlation: String, source: String)

object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {  
  implicit val headerFormat = jsonFormat4(Header1)
  implicit val fooFormats = jsonFormat2(Foo1)
}

// this trait defines our service behavior independently from the service actor
trait RestEndpointService extends HttpService {
  import PersonJsonSupport._
  val restApiRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { 
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("meeting") {
      post {
        entity(as[Foo1]) { someObject =>
          println(someObject)
          complete("Got it!")
        }

      }
    }
    

}


