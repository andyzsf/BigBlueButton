postal = require 'postal'
esl    = require 'modesl'
{parseString}     = require 'xml2js'
log    = require './logger'
config = require '../config'

module.exports = class Connection

  connect: ->
    log.debug("Trying to connect...")
    @conn = new esl.Connection('127.0.0.1', 8021, 'ClueCon', () =>

      @conn.on('esl::event::**', (evt) =>
        @handleFsEslMessage(evt)
      )

      @conn.on('esl::end', () ->
        publishConnectionEvent('Disconnected')
      )  
        #send the status api command
      @conn.events('plain', 'all', (res) =>
        log.debug("Connected")
        publishConnectionEvent('Connected')
        @getAllMeetings()
      )
    ) 

  handleFsEslMessage: (evt) ->   
    evtSubclass = evt.getHeader("Event-Subclass")
    if evtSubclass? and evtSubclass is "conference::maintenance"
      evtFunc = evt.getHeader("Event-Calling-Function")
      if evtFunc? 
        console.log(evt)
        if evtFunc is "conference_add_member"
          @handleUserJoinedMessage(evt)
        else if evtFunc is "conference_del_member"
          @handleUserLeftMessage(evt)
        else if evtFunc is "conf_api_sub_mute"
          @handleUserMutedMessage(evt)
        else if evtFunc is "conf_api_sub_unmute"
          @handleUserMutedMessage(evt)
        else if evtFunc is "conference_loop_input"
          @handleUserTalkingMessage(evt)
        else if evtFunc is "conference_record_thread_run"
          @handleConferenceRecordMessage(evt)

  getUserStatusFromEvent: (evt) ->
    evtConfName = evt.getHeader("Conference-Name")
    userId = evt.getHeader("Member-ID")
    callerNum = evt.getHeader("Caller-Caller-ID-Number")
    callerName = evt.getHeader("Caller-Caller-ID-Name")
    speak = evt.getHeader("Speak")
    talking = evt.getHeader("Talking")
    bbbUserId = evt.getHeader("variable_bbb_userid")
    bbbUserRole = evt.getHeader("variable_bbb_user_role")
    bbbUserPin = evt.getHeader("variable_bbb_user_pin")
    bbbUserListeningOnly = evt.getHeader("variable_bbb_user_listening_only")
    bbbUserCalledFromBbb = evnt.getHeader("variable_bbb_user_called_from_bbb")

    userStatus = new Object()
    userStatus.confId = evtConfName
    userStatus.userId = userId
    userStatus.callerNum = callerNum
    userStatus.callerName = callerName
    userStatus.muted = String(speak).trim().toLowerCase() isnt 'true'
    userStatus.talking = String(talking).trim().toLowerCase().trim() is 'true'    
    userStatus.bbbUserId = bbbUserId
    userStatus.bbbUserRole = bbbUserRole
    userStatus.bbbUserPin = bbbUserPin
    userStatus.bbbUserListeningOnly = bbbUserListeningOnly
    userStatus.bbbUserCalledFromBbb = bbbUserCalledFromBbb

    userStatus

  buildMessageFromUserStatus: (userStatus) ->

    msg = new Object()
    msg.confId = userStatus.confId
    msg.userId = userStatus.userId
    msg.callerNum = userStatus.callerNum
    msg.callerName = userStatus.callerName
    msg.muted = userStatus.muted
    msg.talking = userStatus.talking    
    msg.bbbUserId = userStatus.bbbUserId
    msg.bbbUserRole = userStatus.bbbUserRole
    msg.bbbUserPin = userStatus.bbbUserPin
    msg.bbbUserListeningOnly = userStatus.bbbUserListeningOnly
    msg.bbbUserCalledFromBbb = userStatus.bbbUserCalledFromBbb    

    msg

  handleUserLeftMessage: (evt) ->
    userStatus = getUserStatusFromEvent(evt)
    msg = buildMessageFromUserStatus(userStatus)
    msg.name = "user_left_voice_conference"
    @publishFreeSwitchEvent(msg)

  handleUserMutedMessage: (evt) ->
    userStatus = getUserStatusFromEvent(evt)
    msg = buildMessageFromUserStatus(userStatus)
    msg.name = "user_muted_voice_conference"
    @publishFreeSwitchEvent(msg)

  handleUserTalkingMessage: (evt) ->
    userStatus = getUserStatusFromEvent(evt)
    msg = buildMessageFromUserStatus(userStatus)
    msg.name = "user_talking_voice_conference"
    @publishFreeSwitchEvent(msg)

  handleConferenceRecordMessage: (evt) ->


  handleUserJoinedMessage: (evt) ->
    userStatus = getUserStatusFromEvent(evt)
    msg = buildMessageFromUserStatus(userStatus)
    msg.name = "user_joined_voice_conference"
    @publishFreeSwitchEvent(msg)

  publishFreeSwitchEvent: (event) ->
    postal.publish
      channel: 'from-freeswitch'
      topic: 'broadcast'
      data: event

  getAllMeetings: () ->
    message = "conference xml_list"
    @conn.api(message, (res) ->
      console.log("Response to #{message}:\n" + res.getBody())
      parseString(res.getBody(), (err, result) =>
        console.log(JSON.stringify(result))
      )  
    )    

  sendMessage: (message) ->
    @conn.api(message, (res) ->
      console.log("Response to #{message}:\n" + res.getBody())
    )

  publishConnectionEvent = (event) ->
    console.log("Publish connected message")
    postal.publish
      channel: 'connection'
      topic: 'from-connection'
      data: event