express = require 'express'
routes  = require './routes'
http    = require 'http'
path    = require 'path'
bodyParser = require 'body-parser'
serveStatic = require 'serve-static'

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
app = express()
config.modules.register "App", app

module.exports = app

app.set('port', process.env.PORT || 3004)
app.set('views', __dirname + '/views')
app.set('view engine', 'ejs')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(app.router)
app.use(serveStatic(path.join(__dirname, 'public')))



# Router
config.modules.register "MainRouter", new MainRouter()

# Application modules
config.modules.register "RedisPubSub", new RedisPubSub()
config.modules.register "RedisDbStore", new RedisDbStore()
config.modules.register "AccessCodes", new AccessCodes()
config.modules.register "Controller", new Controller()

http.createServer(app).listen(app.get('port'), () ->
  log.info("Express server listening on port " + app.get('port'));
)
