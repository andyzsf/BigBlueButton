redis = require 'redis'
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
    @subClient.psubscribe("bigbluebutton:from-bbb-apps:*")

  # Send a message without waiting for a reply
  send: (message, envelope) ->
    @publish("bigbluebutton:to-bbb-apps:*", JSON.stringify(message))

  _onSubscribe: (channel, count) =>
    console.log("Subscribed to #{channel}")

  _onMessage: (pattern, channel, jsonMsg) =>
    # this has to be in a try/catch block, otherwise the server will
    #   crash if the message has a bad format
    try

      message = JSON.parse(jsonMsg)
      console.log("Rx Redis : \n" + JSON.stringify(message))
      sendToController (message)
    catch error
      log.error(error)
    finally
    # Do nothing as message has been discarded

  publish: (channel, message) =>
    console.log '\n Publishing\n'
    @pubClient.publish(channel, JSON.stringify(message))

sendToController = (message) ->
  postal.publish
    channel: 'pubsub'
    topic: "from-pubsub"
    data: message
