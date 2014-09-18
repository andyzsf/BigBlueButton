package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceRecordingStarted implements IMessage {
	public static final String VOICE_RECORDING_STARTED  = "voice_recording_started_event";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
	public final String userId;
	public final Boolean recording;
	
	public VoiceRecordingStarted(String confId, String userId, Boolean recording) {
	  this.confId = confId;
	  this.userId = userId;
	  this.recording = recording;
	}
	
	public static VoiceRecordingStarted fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_RECORDING_STARTED.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.USER_ID)
							&& payload.has(Constants.RECORDING)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						Boolean recording = payload.get(Constants.RECORDING).getAsBoolean();
						return new VoiceRecordingStarted(voiceConf, userid, recording);						
					}
				} 
			}
		}
		return null;
	}
}
