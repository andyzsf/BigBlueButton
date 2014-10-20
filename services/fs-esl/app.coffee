log = require './lib/logger'
FreeSwitchEsl = require './lib/fsesl'

fsesl = new FreeSwitchEsl
log.debug("Starting")
fsesl.start()
 



