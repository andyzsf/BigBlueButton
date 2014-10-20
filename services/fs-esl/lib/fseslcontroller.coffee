postal = require 'postal'
{parseString} = require 'xml2js'
timestamp = require 'monotonic-timestamp'
config = require '../config'
log    = require './logger'

module.exports = class FsEslController
  constructor: (connection, builder) ->
    @connection = connection
    @builder = builder

  handleMessage: (message) ->    
    if message.name? 
      if message.name is "user_joined_voice_conference"
        @handleUserJoinedVoiceConferenceEvent(message)
      else if message.name is "user_left_voice_conference"
        @handleUserLeftVoiceConferenceEvent(message)
      else if message.name is "user_muted_voice_conference"
        @handleUserMutedVoiceConferenceEvent(message)
      else if message.name is "user_talking_voice_conference"
        @handleUserTalkingVoiceConferenceEvent(message)
    else
      log.info({message: message}, "Unhandled message")
    
  handleUserLeftVoiceConferenceEvent: (message) ->
    log.info({message: message}, "handleUserLeftVoiceConferenceEvent")
    @handleUserStatusChanged(message)

  handleUserJoinedVoiceConferenceEvent: (message) ->
    log.info({message: message}, "handleUserJoinedVoiceConferenceEvent")
    @handleUserStatusChanged(message)

  handleUserMutedVoiceConferenceEvent: (message) ->
    log.info({message: message}, "handleUserMutedVoiceConferenceEvent")
    @handleUserStatusChanged(message)

  handleUserTalkingVoiceConferenceEvent: (message) ->
    log.info({message: message}, "handleUserTalkingVoiceConferenceEvent")
    @handleUserStatusChanged(message)

  handleUserStatusChanged: (message) ->
    log.info({message: message}, "handleUserStatusChanged")

    confId = message.confId
    userId = message.userId    
    muted = message.muted
    talking = message.talking
    bbbUserId = message.bbbUserId
    bbbUserPin = message.bbbUserPin
    listenOnly = message.bbbUserListeningOnly
    calledFromBbb = message.bbbUserCalledFromBbb

    if listenOnly
      log.info("Caller #{userId} joined conf=[#{confId}] as listenOnly=#{listenOnly}.")
    else
      log.info("Caller #{userId} joined conf=[#{confId}] as not listenOnly=#{listenOnly}")
      @sendVoiceUserStatusChanged(confId, userId, userName, bbbUserPin, muted, talking, bbbUserId, calledFromBbb)      


  handleUserLeftEvent: (message) ->
    log.info({message: message}, "DropParticipantNotify")
    confId = message.DropParticipantNotify.ConferenceId[0]
    userId = message.DropParticipantNotify.CallId[0]
    @sendVoiceUserHasLeft(confId, userId)

  sendVoiceConferenceRecordingStarted: (confId, filename, recordingTimestamp) ->
    header = {}
    header.timestamp = timestamp()
    header.name = "voice_conference_recording_started_message"
    header.current_time = Date.now()

    payload = {}
    payload.voice_conf = confId
    payload.filename = filename
    payload.timestamp = recordingTimestamp

    message = {}
    message.header = header
    message.payload = payload

    sendToRedis(message)      

  sendVoiceConferenceRecordingStopped: (confId, recordingTimestamp) ->
    header = {}
    header.timestamp = timestamp()
    header.name = "voice_conference_recording_stopped_message"
    header.current_time = Date.now()

    payload = {}
    payload.voice_conf = confId
    payload.timestamp = recordingTimestamp

    message = {}
    message.header = header
    message.payload = payload

    sendToRedis(message)     

  sendVoiceUserStatusChanged: (confId, userId, userName, authCode, muted, talking, bbbUserId, calledFromBbb) ->
    header = {}
    header.timestamp = timestamp()
    header.name = "voice_user_status_changed_message"
    header.current_time = Date.now()

    payload = {}
    payload.voice_conf = confId
    payload.userid = userId
    payload.name = userName
    payload.auth_code = authCode
    payload.muted     = muted
    payload.talking = talking
    payload.bbb_userid = bbbUserId
    payload.called_from_bbb = calledFromBbb

    message = {}
    message.header = header
    message.payload = payload

    sendToRedis(message)    

  sendVoiceUserHasLeft: (confId, userId) ->
    header = {}
    header.timestamp = timestamp()
    header.name = "voice_user_left_message"
    header.current_time = Date.now()

    payload = {}
    payload.voice_conf = confId
    payload.userid = userId

    userLeftMessage = {}
    userLeftMessage.header = header
    userLeftMessage.payload = payload

    sendToRedis(userLeftMessage)

  sendToRedis = (message) ->
    log.info({message: message}, "send To Redis")
    postal.publish
      channel: 'pubsub'
      topic: "to-pubsub"
      data: message