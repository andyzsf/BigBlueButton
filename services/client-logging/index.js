
var JL = require('jsnlog').JL;
var jsnlog_nodejs = require('jsnlog-nodejs').jsnlog_nodejs;
var winston = require('winston');

var express = require('express');
var serveStatic = require('serve-static');
var bodyParser = require('body-parser'); 
var app = express();

// The JSON object passed to the MongoDB function below contains options for the 
// MongoDB transport. See the documentation for your chosen transport for its options.
// The options given here are just examples.
var file_appender = new winston.transports.File({ filename: 'development/bbb-client.log', maxSize: 5000000, maxFiles: 5 });
JL().setOptions({ "appenders": [file_appender] });

app.use(serveStatic('public'))

JL().info('log message from server');

var pageHtml =
    "<html>\n" +
    "<head>\n" +
    "</head>\n" +
    "<body>\n" +
    "<h1>Nothing here</h1>\n" +
    "</body>\n" +
    "</html>";

app.get('/', function (req, res) {
    res.send(pageHtml);
});

// -------------------------------
// Receive and process log messages from the client

// parse application/json.
// Log messages from the client use POST and have a JSON object in the body.
// Ensure that those objects get parsed correctly.
app.use(bodyParser.json())

// jsnlog.js on the client by default sends log messages to jsnlog.logger, using POST.
app.post('*.logger', function (req, res) { 
    // Process incoming log messages, by handing to the server side jsnlog.
    // JL is the object that you got at
    // var JL = require('jsnlog').JL;
    jsnlog_nodejs(JL, req.body);

    // Send empty response. This is ok, because client side jsnlog does not use response from server.
    res.send(''); 
});

// -------------------------------

// Start listening for web requests on port 8080
app.listen(8090);


