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
    payload.put(Constants.VOICE_USER, msg.userId)
    payload.put(Constants.MUTE, msg.mute)
    
    val header = Util.buildHeader(MessageNames.UNDO_WHITEBOARD, msg.version, None)
    Util.buildJson(header, payload)
  }  
  
  def ejectVoiceUserToJson(msg: EjectVoiceUser):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.REQUESTER_ID, msg.requesterID)
    payload.put(Constants.VOICE_USER, msg.userId)

    val header = Util.buildHeader(MessageNames.WHITEBOARD_ENABLED, msg.version, None)
    Util.buildJson(header, payload)
  }

  def ejectAllVoiceUsersToJson(msg: EjectAllVoiceUsers):String = {
    val payload = new java.util.HashMap[String, Any]()
    payload.put(Constants.MEETING_ID, msg.meetingID)
    payload.put(Constants.VOICE_CONF, msg.voiceBridge)
 
    val header = Util.buildHeader(MessageNames.IS_WHITEBOARD_ENABLED_REPLY, msg.version, None)
    Util.buildJson(header, payload)
  }  
}