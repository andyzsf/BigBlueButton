request = require 'request'
fs      = require 'fs'
log     = require '../lib/logger'
pins    = require '../lib/pins'
config = require '../config'

# HANDLER
exports.dialplan = (req, resp) -> 
  log.info({request: req.body}, "Received incoming call.")

  destnum = req.param("Caller-Destination-Number")
  
  if destnum?
    if destnum is "9196"
      resp.render('echo')
    else if destnum.length is config.voiceConf.pinLength  
      pins.getUserWithPin(destnum, (err, res) ->
        if res?
          role = if res.role is "MODERATOR" then "512" else "0"
          log.info("dialplan: num=#{destnum} confId=#{res.voiceConf} acode=#{res.userId} role=#{role} did=#{res.dialNumber}")
          resp.render('valid', {destNum: destnum, confId: res.voiceConf, accessCode: res.pin, confCtrls: role, did: res.dialNumber, sip_host: config.te.host})

        if err?
          resp.render(reject, {pin: destnum})
      )
    else if destnum.length is config.voiceConf.length
      username = req.param("Caller-Caller-ID-Name")
      userid = ""

      if username?
        [userid, listenOnly] = getUserIdAndListenOnly(username)

        if userid isnt ""
          callIntoConferenceUsingUserid(destnum, userid, listenOnly, resp)
        else
          log.error("Rejecting call. Invalid userid.")
          rejectCall("Invalid userid.")
      else
        log.error("Cannot get username.")
        rejectCall("Cannot get username.")
    else
      log.error("Rejecting call. Invalid length [#{destnum.length}]")
      rejectCall("Invalid length.")
  else
    log.error("Rejecting call. Missing destination number.")
    rejectCall("Missing destination number")

rejectCall = (message) ->
  resp.render(reject, {message: message})

callIntoConferenceUsingUserid = (destnum, userid, listenOnly, resp) ->
  log.info("User #{userid} is calling into #{destnum} in listenOnly=[#{listenOnly}] mode.")
  pins.getUserWithUserid(destnum, userid, (err, result) ->
    if result?
      role = if result.role is "MODERATOR" then "512" else "0"
      acode = if listenOnly is true then "998" + result.pin else "999" + result.pin
      log.info("dialplan: num=#{destnum} confId=#{result.voiceConf} acode=#{acode} role=#{role} did=#{result.dialNumber}")
      resp.render('valid', {destNum: destnum, confId: result.voiceConf, accessCode: acode, confCtrls: role, did: result.dialNumber, sip_host: config.te.host})

    if err?
      resp.render(reject, {pin: destnum})     
  ) 

getUserIdAndListenOnly = (username) ->
  listenOnly = false
  userNamePattern = /^(.*)-bbbID-(.*)$/
  match = username.match userNamePattern
  if match
    userid = match[1]
  else
    listenOnly = true
    userNamePattern = /^GLOBAL_AUDIO_(\d+)_(.*)$/
    match = username.match userNamePattern
    if match
      userid = match[2] 

  [userid, listenOnly]
  

