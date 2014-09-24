package org.bigbluebutton.api.messaging.messages;

public class DeletePinsMessage implements IMessage {

	public final String meetingId;
	public final String voiceConf;
	
	public DeletePinsMessage(String meetingId, String voiceConf) {
		this.meetingId = meetingId;
		this.voiceConf = voiceConf;
	}
}
