bunyan = require 'bunyan'

logger = bunyan.createLogger({
  name: 'bbbnode',
  streams: [
    {
      level: 'debug',
      stream: process.stdout,
    },
    {
      level: 'info',
      path: '/var/log/bigbluebutton/fs-dialplan.log'
    }
  ]
});

module.exports = logger;
