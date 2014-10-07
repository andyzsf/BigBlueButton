package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceConferenceRecordingStarted implements IMessage {
	public static final String VOICE_CONFERENCE_RECORDING_STARTED  = "voice_conference_recording_started_message";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
  public final String timestamp;
  public final String filename;
	
	public VoiceConferenceRecordingStarted(String confId, String filename, String timestamp) {
	  this.confId = confId;
	  this.filename = filename;
	  this.timestamp = timestamp;
	}
	
	public static VoiceConferenceRecordingStarted fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_CONFERENCE_RECORDING_STARTED.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.TIMESTAMP)
							&& payload.has(Constants.FILENAME)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String timestamp = payload.get(Constants.TIMESTAMP).getAsString();
						String filename = payload.get(Constants.FILENAME).getAsString();
						return new VoiceConferenceRecordingStarted(voiceConf, filename, timestamp);						
					}
				} 
			}
		}
		return null;
	}
}
