
express     = require("express")
redis       = require("redis")
config      = require("./config")
AccessCodes = require("./lib/accesscodes")
Controller  = require("./lib/controller")
Logger      = require("./lib/logger")
MainRouter  = require("./routes/main_router")
Modules     = require("./lib/modules")
RedisPubSub = require("./lib/redispubsub")

# Module to store the modules registered in the application
config.modules = modules = new Modules()

# The application, exported in this module
app = config.modules.register "App", express()
module.exports = app

# configure the application
app.configure ->
  app.set "views", __dirname + "/views"
  app.set "view engine", "ejs"
  app.use express["static"](__dirname + "/public")
  app.use require('connect-assets')()
  app.use express.bodyParser()
  app.use express.methodOverride()
  app.use express.cookieParser()
  app.use express.session()

  app.use app.router

app.configure "development", ->
  app.use express.errorHandler(
    dumpExceptions: true
    showStack: true
  )

app.configure "production", ->
  app.use express.errorHandler()

# view helpers
app.helpers
  h_environment: app.settings.env

# Router
config.modules.register "MainRouter", new MainRouter()

# Application modules
config.modules.register "RedisPubSub", new RedisPubSub()
config.modules.register "RedisDbStore", new RedisDbStore()
config.modules.register "AccessCodes", new AccessCodes()
config.modules.register "Controller", new Controller()

clientProxy = new ClientProxy()
config.modules.register "ClientProxy", clientProxy
clientProxy.listen(app)
