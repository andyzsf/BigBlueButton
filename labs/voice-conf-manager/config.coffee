# # Global configurations file

config = {}

# Default global variables
config.appName = 'BigBlueButton Voice Conference Manager'

config.redis = {}
config.redis.host = '127.0.0.1'
config.redis.port = 6379

# Logging
config.log = {}

config.log.path = if process.env.NODE_ENV == "production"
  "/var/log/bigbluebutton/vcm.log"
else
  "./log/development.log"

# Global instance of Modules, created by `app.coffee`
config.modules = null

module.exports = config