
module.exports = class AccessCodes
  @accessCodes = {}

  addAccessCode: (code) ->
    if code.accessCode?
      @accessCodes[code.accessCode] = code

   