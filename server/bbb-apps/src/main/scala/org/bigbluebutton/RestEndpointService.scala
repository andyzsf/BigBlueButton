package org.bigbluebutton

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.routing.directives.BasicDirectives._
import spray.routing.Directive.pimpApply
import shapeless._
import spray.json.DefaultJsonProtocol

class RestEndpointServiceActor extends Actor with RestEndpointService {

  def actorRefFactory = context

  def receive = runRoute(restApiRoute)
}

// this trait defines our service behavior independently from the service actor
trait RestEndpointService extends HttpService {

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
    } 
}