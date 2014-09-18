package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceUserLeft implements IMessage {
	public static final String VOICE_USER_LEFT  = "voice_user_left_event";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
	public final String userId;
	
	public VoiceUserLeft(String confId, String userId) {
	  this.confId = confId;
	  this.userId = userId;
	}
	
	public static VoiceUserLeft fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_USER_LEFT.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.USER_ID)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						return new VoiceUserLeft(voiceConf, userid);						
					}
				} 
			}
		}
		return null;
	}
}
