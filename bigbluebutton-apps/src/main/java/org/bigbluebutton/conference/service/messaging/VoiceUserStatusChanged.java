package org.bigbluebutton.conference.service.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VoiceUserStatusChanged implements IMessage {
	public static final String VOICE_USER_STATUS_CHANGED  = "voice_user_status_changed_message";
	public static final String VERSION = "0.0.1";
	
	public final String confId;
	public final String userId;
	
	public VoiceUserStatusChanged(String confId, String userId) {
	  this.confId = confId;
	  this.userId = userId;
	}
	
	public static VoiceUserStatusChanged fromJson(String message) {
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(message);
		
		if (obj.has("header") && obj.has("payload")) {
			JsonObject header = (JsonObject) obj.get("header");
			JsonObject payload = (JsonObject) obj.get("payload");
			
			if (header.has("name")) {
				String messageName = header.get("name").getAsString();
				if (VOICE_USER_STATUS_CHANGED.equals(messageName)) {
					if (payload.has(Constants.VOICE_CONF) 
							&& payload.has(Constants.USER_ID)
							&& payload.has(Constants.NAME)
							&& payload.has(Constants.AUTH_CODE)
							&& payload.has(Constants.MUTED)
							&& payload.has(Constants.TALKING)) {
						String voiceConf = payload.get(Constants.VOICE_CONF).getAsString();
						String userid = payload.get(Constants.USER_ID).getAsString();
						return new VoiceUserStatusChanged(voiceConf, userid);						
					}
				} 
			}
		}
		return null;
	}
}
