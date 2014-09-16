package org.bigbluebutton.api.vo;

import java.util.Map;

import org.bigbluebutton.api.domain.Meeting;

public class MeetingVO {
	public final String name;
	public final String externalId;
	public final String internalId;	
	public final long duration;	 
	public final String telVoice;
	public final String webVoice;
	public final String moderatorPass;
	public final String viewerPass;
	public final String welcomeMsg;
	public final String modOnlyMessage;
	public final String logoutUrl;
	public final int maxUsers;
	public final boolean record;
	public final String dialNumber;
	public final String defaultAvatarURL;
	public final String defaultConfigToken;
	public final Map<String, String> extraData;
	
	private MeetingVO(String name, String externalId, String internalId,	
	                  long duration, String telVoice,	String webVoice,
	                  String moderatorPass, String viewerPass, String welcomeMsg,
	                  String modOnlyMessage, String logoutUrl, int maxUsers,
	                  boolean record, String dialNumber, String defaultAvatarURL,
	                  String defaultConfigToken, Map<String, String> extraData, Meeting meeting) {
		this.name = meeting.getName();
		this.externalId = meeting.getExternalId();
		this.internalId = meeting.getInternalId();	
		this.duration = meeting.getDuration();	 
		this.telVoice = telVoice;
		this.webVoice = webVoice;
		this.moderatorPass = moderatorPass;
		this.viewerPass = viewerPass;
		this.welcomeMsg = welcomeMsg;
		this.modOnlyMessage = modOnlyMessage;
		this.logoutUrl = logoutUrl;
		this.maxUsers = maxUsers;
		this.record = record;
		this.dialNumber = dialNumber;
		this.defaultAvatarURL = defaultAvatarURL;
		this.defaultConfigToken = defaultConfigToken;
		this.extraData = extraData;		
	}
}
