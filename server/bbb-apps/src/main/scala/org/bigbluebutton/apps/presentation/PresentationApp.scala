package org.bigbluebutton.apps.presentation

import org.bigbluebutton.apps.presentation.data._

class PresentationApp {
  private var presentations = new collection.immutable.HashMap[String, Presentation]()
  
  private var currentPresentation:Option[Presentation] = None
  
  private def savePresentation(p: Presentation) = {
    presentations += p.id -> p
  }
  
  private def getPresentation(id: String):Option[Presentation] = {
    presentations.get(id)
  }
  
  private def getPage(pres: Presentation, page: Int):Option[Page] = {
    pres.pages find { x => x.num == page}
  }
  
  def newPresentation(pres: Presentation) = {
    savePresentation(pres)
  }
  
  def sharePresentation(id: String):Option[Presentation] = {
    val pres = presentations.get(id) 
    pres foreach { p => currentPresentation = Some(p) }  
    pres
  }
   
  def setCurrentPageForPresentation(pres: Presentation, page: Page):Option[Presentation] = {
    val newPres = pres.copy(currentPage = page.num)
    savePresentation(newPres)
    Some(newPres)
  }
 
  def displayPage(presentation: String, num: Int):Option[Page] = {
    for { 
      pres <- getPresentation(presentation) 
      page <-  getPage(pres, num) 
      newpres <- setCurrentPageForPresentation(pres, page)
    } yield page    
  }
  

}