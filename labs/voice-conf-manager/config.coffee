# # Global configurations file

config = {}

config.redis = {}
config.redis.host = '127.0.0.1'
config.redis.port = 6379

# Logging
config.log = {}

config.log.path = if process.env.NODE_ENV == "production"
  "/var/log/bigbluebutton/vcm.log"
else
  "./log/development.log"

module.exports = config