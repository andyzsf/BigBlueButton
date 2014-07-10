request = require 'request'
fs      = require 'fs'
log     = require '../lib/logger'
acodes  = require '../lib/accesscodes'

db = new AccessCodes

# HANDLER
exports.dialplan = (req, res) -> 
  log.info({request: req.body}, "Received incoming call.")

  destNum = req.param("Caller-Destination-Number")
  
  if destnum?
    ac = db.getAccessCode(destNum)
    if ac?
      res.render('dialplan', {info: ac})
    else
      res.render('reject')


    

    
  

