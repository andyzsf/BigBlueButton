package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceUserTalking implements IMessage {
	public static final String VOICE_USER_TALKING  = "voice_user_talking_event";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
	public final String userId;
	public final Boolean talking;
	
	public VoiceUserTalking(String confId, String userId, Boolean talking) {
	  this.confId = confId;
	  this.userId = userId;
	  this.talking = talking;
	}
	
	public static VoiceUserTalking fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_USER_TALKING.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.USER_ID)
							&& payload.has(Constants.TALKING)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						Boolean talking = payload.get(Constants.TALKING).getAsBoolean();
						return new VoiceUserTalking(voiceConf, userid, talking);						
					}
				} 
			}
		}
		return null;
	}
}
