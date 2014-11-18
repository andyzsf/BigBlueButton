package org.bigbluebutton.conference.meeting.messaging.redis;

import java.util.HashMap;
import java.util.Map;

import org.bigbluebutton.conference.service.messaging.CreateMeetingMessage;
import org.bigbluebutton.conference.service.messaging.DestroyMeetingMessage;
import org.bigbluebutton.conference.service.messaging.EndMeetingMessage;
import org.bigbluebutton.conference.service.messaging.IMessage;
import org.bigbluebutton.conference.service.messaging.KeepAliveMessage;
import org.bigbluebutton.conference.service.messaging.MessageFromJsonConverter;
import org.bigbluebutton.conference.service.messaging.MessagingConstants;
import org.bigbluebutton.conference.service.messaging.RegisterUserMessage;
import org.bigbluebutton.conference.service.messaging.UserConnectedToGlobalAudio;
import org.bigbluebutton.conference.service.messaging.UserDisconnectedFromGlobalAudio;
import org.bigbluebutton.conference.service.messaging.ValidateAuthTokenMessage;
import org.bigbluebutton.conference.service.messaging.VoiceConferenceRecordingStarted;
import org.bigbluebutton.conference.service.messaging.VoiceConferenceRecordingStopped;
import org.bigbluebutton.conference.service.messaging.VoiceRecordingStarted;
import org.bigbluebutton.conference.service.messaging.VoiceUserLeft;
import org.bigbluebutton.conference.service.messaging.VoiceUserStatusChanged;
import org.bigbluebutton.conference.service.messaging.VoiceUserTalking;
import org.bigbluebutton.conference.service.messaging.redis.MessageHandler;
import org.bigbluebutton.core.api.IBigBlueButtonInGW;


import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

public class MeetingMessageHandler implements MessageHandler {
	private static Logger log = Red5LoggerFactory.getLogger(MeetingMessageHandler.class, "bigbluebutton");
	
	private IBigBlueButtonInGW bbbGW;
	
	@Override
	public void handleMessage(String pattern, String channel, String message) {
		log.debug("Handling message: " + pattern + " " + channel + " " + message);
		if (channel.equalsIgnoreCase(MessagingConstants.TO_MEETING_CHANNEL)) {
//			System.out.println("Meeting message: " + channel + " " + message);
			IMessage msg = MessageFromJsonConverter.convert(message);
			
			if (msg != null) {
				if (msg instanceof EndMeetingMessage) {
					EndMeetingMessage emm = (EndMeetingMessage) msg;
					log.debug("Received end meeting request. Meeting id [{}]", emm.meetingId);
					bbbGW.endMeeting(emm.meetingId);
				} else if (msg instanceof CreateMeetingMessage) {
					CreateMeetingMessage emm = (CreateMeetingMessage) msg;
					bbbGW.createMeeting2(emm.id, emm.name, emm.record, emm.voiceBridge, 
							  emm.duration, emm.autoStartRecording, emm.allowStartStopRecording);
				} else if (msg instanceof RegisterUserMessage) {
					RegisterUserMessage emm = (RegisterUserMessage) msg;
					log.debug("Received RegisterUserMessage. user id [{}] pin=[{}]", emm.fullname, emm.pin);
					bbbGW.registerUser(emm.meetingID, emm.internalUserId, emm.fullname, emm.role, emm.externUserID, emm.authToken, emm.pin);
				} else if (msg instanceof DestroyMeetingMessage) {
					DestroyMeetingMessage emm = (DestroyMeetingMessage) msg;
					log.debug("Received destroy meeting request. Meeting id [{}]", emm.meetingId);
					bbbGW.destroyMeeting(emm.meetingId);
				} else if (msg instanceof ValidateAuthTokenMessage) {
					ValidateAuthTokenMessage emm = (ValidateAuthTokenMessage) msg;
					log.debug("Received ValidateAuthTokenMessage token request. Meeting id [{}]", emm.meetingId);
					bbbGW.validateAuthToken(emm.meetingId, emm.userId, emm.token, emm.replyTo);
				} else if (msg instanceof UserConnectedToGlobalAudio) {
					UserConnectedToGlobalAudio emm = (UserConnectedToGlobalAudio) msg;
					log.debug("Received UserConnectedToGlobalAudio token request. user id [{}]", emm.name);
					bbbGW.userConnectedToGlobalAudio(emm.voiceConf, emm.userid, emm.name);
				} else if (msg instanceof UserDisconnectedFromGlobalAudio) {
					UserDisconnectedFromGlobalAudio emm = (UserDisconnectedFromGlobalAudio) msg;
					log.debug("Received UserDisconnectedFromGlobalAudio token request. Meeting id [{}]", emm.name);
					bbbGW.userDisconnectedFromGlobalAudio(emm.voiceConf, emm.userid, emm.name);
				}
			}
		} else if (channel.equalsIgnoreCase(MessagingConstants.TO_SYSTEM_CHANNEL)) {
			IMessage msg = MessageFromJsonConverter.convert(message);
			
			if (msg != null) {
				if (msg instanceof KeepAliveMessage) {
					KeepAliveMessage emm = (KeepAliveMessage) msg;
					//log.debug("Received KeepAliveMessage request. Meeting id [{}]", emm.keepAliveId);
					bbbGW.isAliveAudit(emm.keepAliveId);					
				}
			}
		} else if (channel.equalsIgnoreCase(MessagingConstants.FROM_VOICE_CHANNEL)) {
			log.debug("Handle message from voice conference : " + pattern + " " + channel + " " + message);
			IMessage msg = MessageFromJsonConverter.convert(message);
			if (msg != null) {
				if (msg instanceof VoiceUserLeft) {
					VoiceUserLeft emm = (VoiceUserLeft) msg;
					log.debug("Received VoiceUserLeft request. Meeting id [{}]", emm.confId);		
					bbbGW.voiceUserLeftVoiceConf(emm.confId, emm.userId);
				} else if (msg instanceof VoiceUserStatusChanged) {
					VoiceUserStatusChanged emm = (VoiceUserStatusChanged) msg;
					log.debug("Received VoiceUserStatusChanged request. Voice Conf id [" + emm.confId + "}]");		
					bbbGW.voiceUserStatusChanged(emm.confId, emm.userId, emm.username, emm.authCode, emm.muted, emm.talking, emm.bbbUserId, emm.calledFromBbb);
				} else if (msg instanceof VoiceUserTalking) {
					VoiceUserTalking emm = (VoiceUserTalking) msg;
					log.debug("Received VoiceUserTalking request. Meeting id [{}]", emm.confId);				
				} else if (msg instanceof VoiceRecordingStarted) {
					VoiceRecordingStarted emm = (VoiceRecordingStarted) msg;
					log.info("Received VoiceRecordingStarted request. Meeting id [{}]", emm.confId);				
				} else if (msg instanceof VoiceConferenceRecordingStarted) {
					VoiceConferenceRecordingStarted emm = (VoiceConferenceRecordingStarted) msg;
					log.debug("Received VoiceConferenceRecordingStarted request. Meeting id [{}]", emm.confId);		
					bbbGW.voiceConferenceRecordingStarted(emm.confId, emm.filename, emm.timestamp);
				} else if (msg instanceof VoiceConferenceRecordingStopped) {
					VoiceConferenceRecordingStopped emm = (VoiceConferenceRecordingStopped) msg;
					log.debug("Received VoiceConferenceRecordingStopped request. Meeting id [{}]", emm.confId);	
					bbbGW.voiceConferenceRecordingStopped(emm.confId, emm.timestamp);
				} else {
					log.warn("Unknown message: " + pattern + " " + channel + " " + message);
				}
			}			
		} else {
			log.warn("Cant handle message from channel [" + channel + "]");
		}
	}
	
	public void setBigBlueButtonInGW(IBigBlueButtonInGW bbbGW) {
		this.bbbGW = bbbGW;
	}
	
}
