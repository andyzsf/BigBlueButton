package org.bigbluebutton.core.voice.redis

import org.bigbluebutton.core.api._
import org.bigbluebutton.core.messaging.Util
import org.bigbluebutton.core.apps.whiteboard.vo.AnnotationVO
import collection.JavaConverters._
import scala.collection.JavaConversions._

object VoiceConfMessageToJsonConverter {

  def meetingCreatedToJson(msg: MeetingCreated):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    
    val header = Util.buildHeader(MessageNames.MEETING_CREATED, msg.version, None)
    Util.buildJson(header, payload)
  }
  
  def stopRecordingVoiceConferenceToJson(msg: StopRecordingVoiceConference):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.VOICE_CONF, msg.voiceConf)
    
    val header = Util.buildHeader(MessageNames.STOP_RECORDING_VOICE_CONF, msg.version, None)
    Util.buildJson(header, payload)    
  }
  
  def startRecordingVoiceConferenceToJson(msg: StartRecordingVoiceConference):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.VOICE_CONF, msg.voiceConf)
    payload.put(Constants.FILENAME, msg.filename)
    
    val header = Util.buildHeader(MessageNames.START_RECORDING_VOICE_CONF, msg.version, None)
    Util.buildJson(header, payload)    
  }
  
  def meetingEndedToJson(msg: MeetingEnded):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.VOICE_CONF, msg.voiceBridge)
 
    val header = Util.buildHeader(MessageNames.MEETING_ENDED, msg.version, None)
    Util.buildJson(header, payload)
  }
  
  def meetingDestroyedToJson(msg: MeetingDestroyed):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
 
    val header = Util.buildHeader(MessageNames.MEETING_DESTROYED, msg.version, None)
    Util.buildJson(header, payload)
  }
  
  def muteVoiceUserToJson(msg: MuteVoiceUser):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.REQUESTER_ID, msg.requesterID)
    payload.put(Constants.USER_ID, msg.userId)
    payload.put(Constants.VOICE_CONF, msg.voiceConf)
    payload.put(Constants.VOICE_USERID, msg.voiceUserId)
    payload.put(Constants.MUTE, msg.mute)
    
    val header = Util.buildHeader(MessageNames.MUTE_VOICE_USER, msg.version, None)
    Util.buildJson(header, payload)
  }  
  
  def ejectVoiceUserToJson(msg: EjectVoiceUser):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.REQUESTER_ID, msg.requesterID)
    payload.put(Constants.USER_ID, msg.userId)
    payload.put(Constants.VOICE_CONF, msg.voiceConf)
    payload.put(Constants.VOICE_USERID, msg.voiceUserId)

    val header = Util.buildHeader(MessageNames.EJECT_VOICE_USER, msg.version, None)
    Util.buildJson(header, payload)
  }

  def ejectAllVoiceUsersToJson(msg: EjectAllVoiceUsers):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.VOICE_CONF, msg.voiceBridge)
 
    val header = Util.buildHeader(MessageNames.EJECT_ALL_VOICE_USERS, msg.version, None)
    Util.buildJson(header, payload)
  }  
}