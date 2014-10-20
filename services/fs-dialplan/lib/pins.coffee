redis = require 'redis'

config = require '../config'
log = require './logger'

client = redis.createClient()

exports.getUserWithPin = (pin, callback) -> 
    client.hgetall("bbb:voicepin:" + pin, (err, res) ->
      if res?
        callback(null, res)
      else 
        callback("Pin #{pin} not found.", null)
    )

exports.getUserWithUserid = (voiceConf, userid, callback) -> 
    client.hgetall("bbb:voice:user:" + voiceConf + ":" + userid, (err, res) ->
      if res?
        callback(null, res)
      else 
        callback("Pin #{pin} not found.", null)
    )  