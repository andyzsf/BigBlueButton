package org.bigbluebutton.apps.whiteboard.messages

import org.bigbluebutton.apps.whiteboard.data.Foo1



case class AnnotationVO(id: String, status: String, shapeType: String, shape: scala.collection.immutable.Map[String, Object])
case class SendWhiteboardAnnotationRequest(meetingID: String, requesterID: String, annotation: AnnotationVO) 
case class SetWhiteboardActivePageRequest(meetingID: String, requesterID: String, page: Int) 
case class SendWhiteboardAnnotationHistoryRequest(meetingID: String, requesterID: String, presentationID: String, page: Int) 
case class ClearWhiteboardRequest(meetingID: String, requesterID: String) 
case class UndoWhiteboardRequest(meetingID: String, requesterID: String) 
case class SetActivePresentationRequest(meetingID: String, requesterID: String, presentationID: String, numPages: Int) 
case class EnableWhiteboardRequest(meetingID: String, requesterID: String, enable: Boolean) 
case class IsWhiteboardEnabledRequest(meetingID: String, requesterID: String) 

case class WbMsg(fo: Foo1)
