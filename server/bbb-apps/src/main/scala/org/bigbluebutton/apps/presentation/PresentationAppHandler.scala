package org.bigbluebutton.apps.presentation

import akka.actor.{ActorRef, actorRef2Scala}
import akka.event.LoggingAdapter
import org.bigbluebutton.apps.RunningMeetingActor
import org.bigbluebutton.apps.presentation.messages._

trait PresentationAppHandler {
  this : RunningMeetingActor =>
  
  val pubsub: ActorRef
  val log: LoggingAdapter

  val presApp = new PresentationApp()
  
  def handleClearPresentation(msg: ClearPresentation) = {
  //  presApp.clearPresentation(msg.presentation)
  }
  
  def handleRemovePresentation(msg : RemovePresentation) = {
    
  }

  def handleSendCursorUpdate(msg: SendCursorUpdate) = {
    
  }
  
  def handleResizeAndMoveSlide(msg: ResizeAndMovePage) = {
    
  }

  def handleDisplayPage(msg: DisplayPage) = {
    val page = presApp.displayPage(msg.presentation.id, msg.page) 
    page foreach { p =>
      pubsub ! PageDisplayed(session, msg.presentation, p)
    }
  }

  def handleSharePresentation(msg: SharePresentation) = {
    val presentation = presApp.sharePresentation(msg.presentation.id)
    presentation foreach {pres =>
      pubsub ! PresentationShared(session, pres)
      
      self ! DisplayPage(session, msg.presentation, 1)
    }
  }
  
  def handlePreuploadedPresentations(msg: PreuploadedPresentations) = {
    msg.presentations foreach {p => presApp.newPresentation(p)}
  }

  def handlePresentationConverted(msg: PresentationConverted) = {
    presApp.newPresentation(msg.presentation)
  }

}