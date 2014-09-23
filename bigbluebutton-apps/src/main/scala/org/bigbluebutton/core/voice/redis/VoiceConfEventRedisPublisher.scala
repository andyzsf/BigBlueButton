package org.bigbluebutton.core.voice.redis

import org.bigbluebutton.core.api._
import scala.collection.JavaConversions._
import scala.collection.immutable.StringOps
import org.bigbluebutton.conference.service.messaging.redis.MessageSender
import org.bigbluebutton.conference.service.messaging.MessagingConstants

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class VoiceConfEventRedisPublisher(service: MessageSender) extends OutMessageListener2 {
  def handleMessage(msg: IOutMessage) {
	  msg match {
	    case msg: MeetingCreated                => handleMeetingCreated(msg)
	    case msg: MeetingEnded                  => handleMeetingEnded(msg)
	    case msg: MeetingDestroyed              => handleMeetingDestroyed(msg)
	    case msg: MuteVoiceUser                 => handleMuteVoiceUser(msg)
	    case msg: EjectVoiceUser                => handleEjectVoiceUser(msg)
	    case msg: EjectAllVoiceUsers            => handleEjectAllVoiceUsers(msg)
	    case _ => // do nothing
	  }
	}
  	
  private def handleMeetingCreated(msg: MeetingCreated) {
    val json = VoiceConfMessageToJsonConverter.meetingCreatedToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
  
  private def handleMeetingEnded(msg: MeetingEnded) {
    val json = VoiceConfMessageToJsonConverter.meetingEndedToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
  
  private def handleMeetingDestroyed(msg: MeetingDestroyed) {
    val json = VoiceConfMessageToJsonConverter.meetingDestroyedToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
    
  private def handleMuteVoiceUser(msg: MuteVoiceUser) {
    val json = VoiceConfMessageToJsonConverter.muteVoiceUserToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
  
  private def handleEjectVoiceUser(msg: EjectVoiceUser) {
    val json = VoiceConfMessageToJsonConverter.ejectVoiceUserToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
  
  private def handleEjectAllVoiceUsers(msg: EjectAllVoiceUsers) {
    val json = VoiceConfMessageToJsonConverter.ejectAllVoiceUsersToJson(msg)
    service.send(MessagingConstants.TO_VOICE_CHANNEL, json)
  }
}