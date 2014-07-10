redis = require 'redis'
config = require '../config'
log = require './logger'

ACC_KEY = 'bigbluebutton:vcm:accesscodes:'

module.exports = class RedisDbStore
  constructor: ->
    @client = redis.createClient()

  addAccessCode: (code) ->
    if code.accessCode?
      @client.hmset(ACC_KEY + code.accessCode, code)

  getAccessCode: (code) ->
    @client.hgetall(ACC_KEY + code, (err, reply) ->
      if err
        return null

      reply
    )