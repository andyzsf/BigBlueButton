store = require './redisdb'

module.exports = class AccessCodes

  constructor: ->
    @db = new RedisDbStore

  getAccessCode: (code) ->
    accessCode = null
    if code.accessCode?
      accessCode = @db.getAccessCode(code)

    accessCode

  addAccessCode: (code) ->
    @db.addAccessCode(code)

