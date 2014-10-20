bunyan = require 'bunyan'

config = require '../config'

logger = bunyan.createLogger({
  name: 'fsesl',
  streams: [
    {
      level: 'debug',
      stream: process.stdout
    },
    {
      level: 'info',
      path: config.log.path
    }
  ]
})

module.exports = logger