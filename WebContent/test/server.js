var Router = require("vertx-web-js/router");
var SockJSHandler = require("vertx-web-js/sock_js_handler");
var StaticHandler = require("vertx-web-js/static_handler");

var router = Router.router(vertx);

// Allow events for the designated addresses in/out of the event bus bridge
var opts = {
  "inboundPermitteds" : [
    {
      "address" : "chat.to.server"
    }
  ],
  "outboundPermitteds" : [
    {
      "address" : "chat.to.client"
    }
  ]
};

// Create the event bus bridge and add it to the router.
var ebHandler = SockJSHandler.create(vertx).bridge(opts);
router.route("/eventbus/*").handler(ebHandler.handle);

// Create a router endpoint for the static content.
router.route().handler(StaticHandler.create().handle);

// Start the web server and tell it to use the router to handle requests.
vertx.createHttpServer().requestHandler(router.accept).listen(8881);

var eb = vertx.eventBus();

// Register to listen for messages coming IN to the server
eb.consumer("chat.to.server").handler(function (message) {
  // Send the message back out to all clients with the timestamp prepended.
  eb.publish("chat.to.client", message.body());
});

