package org.bigbluebutton.web.services.messages;

public class StorePinMessage implements IVoiceMessage {
	public final String meetingId;
	public final String dialNumber;
	public final String voiceConf;
	public final String pin;
	public final String userId;
	public final String username;
	public final String role;
	
	public StorePinMessage(String meetingId, String dialNumber, String voiceConf, String pin, String userId, String username, String role) {
		this.meetingId = meetingId;
		this.dialNumber = dialNumber;
		this.voiceConf = voiceConf;
		this.pin = pin;
		this.userId = userId;
		this.username = username;
		this.role = role;
	}
}
