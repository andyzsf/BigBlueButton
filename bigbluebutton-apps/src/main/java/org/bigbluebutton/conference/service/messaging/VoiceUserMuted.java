package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceUserMuted implements IMessage {
	public static final String VOICE_USER_MUTED  = "voice_user_muted_event";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
	public final String userId;
	public final Boolean muted;
	
	public VoiceUserMuted(String confId, String userId, Boolean muted) {
	  this.confId = confId;
	  this.userId = userId;
	  this.muted = muted;
	}
	
	public static VoiceUserMuted fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_USER_MUTED.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.USER_ID)
							&& payload.has(Constants.MUTED)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						Boolean muted = payload.get(Constants.MUTED).getAsBoolean();
						return new VoiceUserMuted(voiceConf, userid, muted);						
					}
				} 
			}
		}
		return null;
	}
}
