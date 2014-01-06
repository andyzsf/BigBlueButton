/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
* 
* Copyright (c) 2012 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 3.0 of the License, or (at your option) any later
* version.
* 
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
*
*/
package org.bigbluebutton.conference.service.presentation;

import org.slf4j.Logger;
import org.bigbluebutton.core.api.IBigBlueButtonInGW;
import org.red5.logging.Red5LoggerFactory;
<<<<<<< HEAD
import java.util.Map;


=======
import org.red5.server.api.Red5;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

>>>>>>> html5-bridge-new-events
public class PresentationApplication {
	private static Logger log = Red5LoggerFactory.getLogger( PresentationApplication.class, "bigbluebutton" );	
		
	private IBigBlueButtonInGW bbbInGW;
	
	public void setBigBlueButtonInGW(IBigBlueButtonInGW inGW) {
		bbbInGW = inGW;
	}
	

	
	public void clear(String meetingID) {
		
	}
	
	public void sendUpdateMessage(Map<String, Object> message){	
		String meetingID = (String) message.get("room");		
		bbbInGW.sendUpdateMessage(meetingID, message);
	}
			
	public void removePresentation(String meetingID, String presentationID){
		bbbInGW.removePresentation(meetingID, presentationID);
    }
	
	public void getPresentationInfo(String meetingID, String requesterID) {
		bbbInGW.getPresentationInfo(meetingID, requesterID);
	}
<<<<<<< HEAD
		
	public void sendCursorUpdate(String meetingID, Double xPercent, Double yPercent) {	

		bbbInGW.sendCursorUpdate(meetingID, xPercent, yPercent);
=======
	
	public void sendCursorUpdate(String room, Double xPercent, Double yPercent) {	
		if (roomsManager.hasRoom(room)){
			log.debug("Request to update cursor[" + xPercent + "," + yPercent + "]");
			roomsManager.sendCursorUpdate(room, xPercent, yPercent);
			
			Map<String, Object> message = new HashMap<String, Object>();	
			message.put("xPercent", xPercent);
			message.put("yPercent", yPercent);
			//ClientMessage m = new ClientMessage(ClientMessage.BROADCAST, getMeetingId(), "PresentationCursorUpdateCommand", message);
			ClientMessage m = new ClientMessage(ClientMessage.BROADCAST, room, "PresentationCursorUpdateCommand", message);
			connInvokerService.sendMessage(m);
			
			return;
		}
				
		log.warn("Sending cursor update on a non-existant room " + room);
>>>>>>> html5-bridge-new-events
	}
	
	public void resizeAndMoveSlide(String meetingID, Double xOffset, Double yOffset, Double widthRatio, Double heightRatio) {
		bbbInGW.resizeAndMoveSlide(meetingID, xOffset, yOffset, widthRatio, heightRatio);
	}
		
	public void gotoSlide(String meetingID, int slide){		
		bbbInGW.gotoSlide(meetingID, slide);
	}
	
	public void sharePresentation(String meetingID, String presentationID, Boolean share){		
		bbbInGW.sharePresentation(meetingID, presentationID, share);
	}
	
	public void getSlideInfo(String meetingID, String requesterID) {
		
		bbbInGW.getSlideInfo(meetingID, requesterID);
		
	}
		
}
