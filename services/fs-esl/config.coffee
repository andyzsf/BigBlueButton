# # Global configurations file

config = {}

config.redis = {}
config.redis.publishChannel = "bigbluebutton:to-bbb-apps:voiceconf" 
config.redis.subscribeChannel = "bigbluebutton:from-bbb-apps:voiceconf"

config.voiceConf = {}
config.voiceConf.length = 7
config.voiceConf.pinLength = 5


# Logging
config.log = {}

config.log.path = if process.env.NODE_ENV == "production"
  "/var/log/bigbluebutton/fsesl.log"
else
  "./log/development.log"

module.exports = config

