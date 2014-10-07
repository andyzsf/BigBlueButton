package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceConferenceRecordingStopped implements IMessage {
	public static final String VOICE_CONFERENCE_RECORDING_STOPPED  = "voice_conference_recording_stopped_message";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
  public final String timestamp;
	
	public VoiceConferenceRecordingStopped(String confId, String timestamp) {
	  this.confId = confId;
	  this.timestamp = timestamp;
	}
	
	public static VoiceConferenceRecordingStopped fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_CONFERENCE_RECORDING_STOPPED.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.TIMESTAMP)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String timestamp = payload.get(Constants.TIMESTAMP).getAsString();
						return new VoiceConferenceRecordingStopped(voiceConf, timestamp);						
					}
				} 
			}
		}
		return null;
	}
}
