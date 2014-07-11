express = require 'express'
http    = require 'http'
path    = require 'path'
bodyParser = require 'body-parser'
serveStatic = require 'serve-static'

# The application, exported in this module
app = express()

router = express.Router()
# invoked for any requests passed to this router
router.use( (req, res, next) ->
  # some logic here .. like any other middleware
  console.log("in event")
  next()
)

# will handle any request that ends in /events
# depends on where the router is "use()'d"
router.get('/events', (req, res, next) ->
  console.log("in events")
)

app.set('port', process.env.PORT || 3004)
app.set('views', __dirname + '/views')
app.set('view engine', 'ejs')
app.use(bodyParser.urlencoded({ extended: false }))
app.use(serveStatic(path.join(__dirname, 'public')))
# only requests to /calendar/* will be sent to our "router"
app.use('/calendar', router);

http.createServer(app).listen(app.get('port'), () ->
  console.log("Express server listening on port " + app.get('port'));
)
