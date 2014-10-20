postal            = require 'postal'
{parseString}     = require 'xml2js'
timestamp         = require 'monotonic-timestamp'

log               = require './logger'
MessageBuilder    = require './messagebuilder'
Connection        = require './connection'
RedisPubSub       = require './redispubsub'
FsEslController   = require './fseslcontroller'

module.exports = class FreeSwitchEsl
  constructor: ->
    @builder = new MessageBuilder
    @connection = new Connection
    @pubsub = new RedisPubSub
    @teController = new FsEslController(@connection, @builder)

    @subscribeToFreeSwitchMessages()
    @subscribeConnectionMessage()
    @subscribeToPubSubMessages()

  start: ->
    console.log("Starting...")
    @connection.connect()

  subscribeConnectionMessage: ->
    postal.subscribe
      channel: 'connection'
      topic: "from-connection"
      callback: (msg, envelope) =>
        log.debug("Postal message from connection: \n" + msg)
        @handleConnectionMessage(msg)

  subscribeToPubSubMessages: ->
    postal.subscribe
      channel: 'pubsub'
      topic: "from-pubsub"
      callback: (msg, envelope) =>
        log.debug({message: msg}, "Postal message from pubsub")
        @handlePubSubMessage(msg)

  handlePubSubMessage: (message) ->
    log.debug("Handling pubsub message[" + message.header.name + "]")
    if message.header?.name?
      messageName = message.header.name
      if messageName is "mute_voice_user_request"
        @handleMuteVoiceUserRequest(message)
      else if messageName is "eject_voice_user_message"
        @handleEjectVoiceUserRequest(message)
      else if messageName is "start_recording_voice_conference"
        @handleStartRecordingVoiceConferenceRequest(message)
      else if messageName is "stop_recording_voice_conference"
        @handleStopRecordingVoiceConferenceRequest(message)

  handleStartRecordingVoiceConferenceRequest: (message) ->
    log.debug({message: message}, "Handling start_recording_voice_conference")
    if message.payload?.voice_conf? and message.payload?.meeting_id? and message.payload?.filename
      confId = message.payload.voice_conf
      meetingId = message.payload.meeting_id
      filename = message.payload.filename
      msg = @builder.startRecordingReq(confId, filename)
      @connection.sendMessage(msg)

  handleStopRecordingVoiceConferenceRequest: (message) ->
    log.debug({message: message}, "Handling stop_recording_voice_conference")
    if message.payload?.voice_conf? and message.payload?.meeting_id? and message.payload?.filename
      confId = message.payload.voice_conf
      meetingId = message.payload.meeting_id
      filename = message.payload.filename
      msg = @builder.stopRecordingReq(confId, filename)
      @connection.sendMessage(msg)

  handleMuteVoiceUserRequest: (message) ->
    log.debug({message: message}, "Handling mute_voice_user_request")
    if message.payload?.voice_conf? and message.payload?.voice_userid? and message.payload?.mute?
      confId = confId = message.payload.voice_conf
      userId = message.payload.voice_userid
      mute = message.payload.mute

      if mute 
        log.info({message: message}, "Muting user")
        msg = @builder.muteUserReq(confId, userId)
        @connection.sendMessage(msg)
      else
        log.info({message: message}, "Unmuting user")
        msg = @builder.unmuteUserReq(confId, userId)
        @connection.sendMessage(msg)    

  handleEjectVoiceUserRequest: (message) ->
    log.debug({message: message}, "Handling eject voice user request")
    if message.payload?.voice_conf? and message.payload?.voice_userid?
      confId = message.payload.voice_conf
      userId = message.payload.voice_userid
      msg = @builder.ejectUserReq(confId, userId)
      @connection.sendMessage(msg)
    else
      log.info({message: message}, "Invalid message.")

  handleConnectionMessage: (message) ->
    switch message
      when 'Connected'
        log.info("Got Connected Message")

  subscribeToFreeSwitchMessages: ->
    postal.subscribe
      channel: 'from-freeswitch'
      topic: "broadcast"
      callback: (message, envelope) =>
        @handleFreeSwitchMessage(message)

  handleFreeSwitchMessage: (message) ->
    @teController.handleMessage(message)
    
