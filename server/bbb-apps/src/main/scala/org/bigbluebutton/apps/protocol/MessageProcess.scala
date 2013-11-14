package org.bigbluebutton.apps.protocol

import akka.actor.Actor
import spray.json.JsObject
import org.bigbluebutton.apps.protocol.Message.MessageEvent
import org.bigbluebutton.apps.protocol.Message._

class MessageProcess extends Actor {

  def receive = {
    case MessageEvent(header, payload) => // do something
    case _ => // do nothing
  }
  
  def check( header: MessageHeader, payload: JsObject ) = {
    header.name match {
        case "CreateMeetingRequest"  => 
        case _ =>
    }
  }
}