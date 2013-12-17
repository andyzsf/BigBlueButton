package org.bigbluebutton.apps.presentation.messages

import org.bigbluebutton.apps.models.Session
import org.bigbluebutton.apps.users.data.UserIdAndName
import org.bigbluebutton.apps.presentation.data._

case class ClearPresentation(session: Session, 
                             presentation: PresentationIdAndName,
                             clearedBy: UserIdAndName)
                             
case class RemovePresentation(session: Session, 
                             presentation: PresentationIdAndName,
                             removedBy: UserIdAndName)
                             
case class SendCursorUpdate(session: Session, 
                            xPercent: Double, yPercent: Double)
                            
case class ResizeAndMovePage(session: Session, 
                              presentation: PresentationIdAndName,
                              page: Int,
                              position: Position)
                              
case class DisplayPage(session: Session, 
                       presentation: PresentationIdAndName, 
                       page: Int)

case class PageDisplayed(session: Session, 
                         presentation: PresentationIdAndName,
                         page: Page)                       
                       
case class SharePresentation(session: Session, 
                             presentation: PresentationIdAndName)

case class PresentationShared(session: Session, presentation: Presentation)

case class PreuploadedPresentations(session: Session, 
                                    presentations: Seq[Presentation])
                                    
case class PresentationConverted(session: Session, 
                                 presentation: Presentation)

case class PresentationConversionUpdate(meetingID: String, msg: Map[String, Object])
case class GetPresentationInfo(meetingID: String, requesterID: String) 
case class GetSlideInfo(meetingID: String, requesterID: String) 
