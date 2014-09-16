package org.bigbluebutton.api;

public class VoiceConfService {

	private String voiceAuthToken = "bbbftw";
	
	public String getVoiceAuthToken() {
		return voiceAuthToken;
	}
	
	public void setVoiceAuthToken(String token) {
		voiceAuthToken = token;
	}
}
