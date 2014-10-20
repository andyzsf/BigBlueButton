builder = require('xmlbuilder');

CONF = 'conference'
MUTE = 'mute'
UNMUTE = 'unmute'
KICK_ALL = 'kick all'
SPACE = ' '
RECORD = 'record'
NO_RECORD = 'norecord'

module.exports = class MessageBuilder
  getAllConferenceReq : ->
    "#{CONF} xml_list"

  startRecordingReq : (confId, file) ->
    "#{CONF} #{confId} record #{file}"

  stopRecordingReq : (confId, file) ->
    "#{CONF} #{confId} norecord #{file}"

  ejectUserReq : (confId, userId) ->
    "#{CONF} #{confId} kick #{userId}"

  ejectAllUsersReq: (confId) ->
    "#{CONF} #{confId} kick all"

  muteUserReq : (confId, userId) ->
    "#{CONF} #{confId} mute #{userId}"

  unmuteUserReq : (userId) ->
    "#{CONF} #{confId} unmute #{userId}"
