redis = require 'redis'
crypto = require 'crypto'
postal = require 'postal'

config = require '../config'
log = require './logger'

module.exports = class RedisPubSub

  constructor: ->
    @pubClient = redis.createClient()
    @subClient = redis.createClient()

    postal.subscribe
      channel: 'pubsub'
      topic: 'to-pubsub'
      callback: (msg, envelope) =>
          @send(msg, envelope)

    @subClient.on "psubscribe", @_onSubscribe
    @subClient.on "pmessage", @_onMessage

    log.info("RPC: Subscribing message on channel")
    @subClient.psubscribe(config.redis.subscribeChannel)

  # Send a message without waiting for a reply
  send: (message, envelope) ->
    log.debug({message: message} , "Publishing to [" + config.redis.publishChannel + "]")
    @publish(config.redis.publishChannel, message)

  _onSubscribe: (channel, count) =>
    log.info("Subscribed to #{channel}")

  _onMessage: (pattern, channel, jsonMsg) =>
    # this has to be in a try/catch block, otherwise the server will
    #   crash if the message has a bad format
    try
      message = JSON.parse(jsonMsg)
      log.debug({message: message} , "Received from Redis")
      sendToController(message)
    catch error
      log.error(error)
    finally
      # Do nothing as message has been discarded

  publish: (channel, message) =>
    log.debug({message: message}, "Publishing to redis")
    @pubClient.publish(channel, JSON.stringify(message))

sendToController = (message) ->
  postal.publish
    channel: 'pubsub'
    topic: "from-pubsub"
    data: message