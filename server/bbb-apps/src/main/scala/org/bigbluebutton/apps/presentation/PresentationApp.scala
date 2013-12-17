package org.bigbluebutton.apps.presentation

import org.bigbluebutton.apps.presentation.data._

class PresentationApp {
  private var presentations = new collection.immutable.HashMap[String, Presentation]()
  
  private var currentPresentation:Option[Presentation] = None
  
  private def savePresentation(p: Presentation) = {
    presentations += p.id -> p
  }
  
  def newPresentation(pres: Presentation) = {
    savePresentation(pres)
  }
  
  def sharePresentation(id: String):Option[Presentation] = {
    val pres = presentations.get(id) 
    pres foreach { p => currentPresentation = Some(p) }  
    pres
  }
  
  def displayPage(presentation: String, page: Int):Option[Page] = {
    presentations.get(presentation) match {
      case Some(p) => {
        p.pages find { x => x.num == page} match {
          case Some(m) => {
            val pr = p.copy(currentPage = m.num)
            savePresentation(pr)
            Some(m)
          }
          case None => None
        }
      }
      case None => None
    }
  }
}