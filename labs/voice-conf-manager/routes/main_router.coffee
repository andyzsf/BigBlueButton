config = require("../config")
log    = require '../lib/logger'

moduleDeps = ["App", "AccessCodes"]

# The main router that registers the routes that can be accessed by the client.
module.exports = class MainRouter

  constructor: () ->
    config.modules.wait moduleDeps, =>
      @app = config.modules.get("App")
      @store = config.module.get("AccessCodes")
      @_registerRoutes()

  _registerRoutes: () ->
    @app.get "/", @_index

    @app.get "/proxy", @_proxyHandler


  # Base route to render the HTML5 client.
  #
  # This method is registered as a route on express.
  #
  # @internal
  _index: (req, res) =>
    res.render "reject",
      title: config.appName

  # A route to enter the HTML5 client from a landing page
  #
  # @internal
  _proxyHandler: (req, res) ->
    log.info({request: req.body}, "Received incoming call.")

    destNum = req.param("Caller-Destination-Number")

    if destnum?
      ac = db.getAccessCode(destNum)
      if ac?
        res.render('dialplan', {info: ac})
      else
        res.render('reject')

