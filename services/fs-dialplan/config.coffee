# # Global configurations file

config = {}

config.voiceConf = {}
config.voiceConf.length = 7
config.voiceConf.pinLength = 3

# Logging
config.log = {}

config.log.path = if process.env.NODE_ENV == "production"
  "/var/log/bigbluebutton/fsdialplan.log"
else
  "./log/development.log"

module.exports = config

