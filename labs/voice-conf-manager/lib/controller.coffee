postal = require 'postal'
config = require '../config'
log    = require './logger'

module.exports = class Controller

  constructor: ->
    postal.subscribe
      channel: 'pubsub'
      topic: 'from-pubsub'
      callback: (msg, envelope) =>
        @handleMessage(msg)

  handleMessage: (msg) ->
    console.log("Ctroller rx message: \n " + JSON.stringify(msg))