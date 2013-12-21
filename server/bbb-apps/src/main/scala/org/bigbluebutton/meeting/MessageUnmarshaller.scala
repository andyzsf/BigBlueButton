package org.bigbluebutton.meeting

import spray.json.JsObject
import spray.json.DeserializationException
import org.bigbluebutton.apps.models.MeetingDescriptor
import akka.event.slf4j.SLF4JLogging
import org.bigbluebutton.apps.protocol.InMessage
import org.bigbluebutton.apps.protocol._

trait MeetingMessageHandler extends SLF4JLogging {
  import CreateMeetingRequestJsonProtocol1._
  
  def handleCreateMeetingRequest(header: Header, 
                                 payload: JsObject):InMessage = {
    payload.fields.get("meeting") match {
      case Some(meeting) => {
	      try {
	        val m = meeting.convertTo[Meeting]
	        CreateMeetingRequest(header, m)
	      } catch {
	        case e: DeserializationException => {
	          throw MessageProcessException("Failed to deserialize create meeting message: [" + payload + "]")
	        }
	      }        
      }
      case None => throw MessageProcessException("Malformed message: [" + payload + "]")
    }   
  }
}