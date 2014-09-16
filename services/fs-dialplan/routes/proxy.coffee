request = require 'request'
fs      = require 'fs'
log     = require '../lib/logger'


# HANDLER
exports.dialplan = (req, res) -> 
  log.info({request: req.body}, "Received incoming call.")

  destnum = req.param("Caller-Destination-Number")
  
  # Placeholder
  res.render('reject')

    
  

