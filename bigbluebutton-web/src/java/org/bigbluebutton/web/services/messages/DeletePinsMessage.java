package org.bigbluebutton.web.services.messages;

public class DeletePinsMessage implements IVoiceMessage {

	public final String meetingId;
	
	public DeletePinsMessage(String meetingId) {
		this.meetingId = meetingId;
	}
}
