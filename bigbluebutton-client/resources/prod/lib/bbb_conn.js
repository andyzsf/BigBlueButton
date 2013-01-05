(function(window, undefined) {

    var BBBConn = {};

	/** Stores the 3rd-party app event listeners ***/ 
    var listeners = {};
    /**
     * 3rd-party apps should user this method to register to listen for events.
     */
    BBB.on = function(eventName, handler) {
        if (typeof listeners[eventName] === 'undefined')
            listeners[eventName] = [];
        
        listeners[eventName].push(handler);
    };

    /**
     * 3rd-party app should use this method to unregister listener for a given event.
     */
    BBB.unlisten = function(eventName, handler) {
        if (!listeners[eventName])
            return;
        
        for (var i = 0; i < listeners[eventName].length; i++) {
            if (listeners[eventName][i] === handler) {
                listeners.splice(i, 1);
                break;
            }
        }
    };

    /**
     * Private function to broadcast received event from the BigBlueButton Flash client to
     * 3rd-parties.
     */
    function emit(bbbEvent) {
        if (!listeners[bbbEvent.eventName]) {
            console.log("No listeners for [" + bbbEvent.eventName + "]");        
            return;
        }
        
        for (var i = 0; i < listeners[bbbEvent.eventName].length; i++) {
            console.log("Notifying listeners for [" + bbbEvent.eventName + "]"); 
            listeners[bbbEvent.eventName][i](bbbEvent);
        }
    };

    BBBConn.ready = function() {
    	console.log("ready");
    }

    BBBConn.connected = function () {
    	console.log("Connected");
    }

    BBBConn.connectFailed = function() {
    	console.log("Connect attempt failed");
    }

    BBBConn.disconnected = function() {
    	console.log("Disconnected");
    }



    window.BBBConn = BBBConn;
})(this);