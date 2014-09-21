
(function(window, undefined) {

    var BBBLog = {};

	JL.setOptions({
       "enabled": true,
       "requestId": "35F7416D-86F1-47FA-A9EC-547FFF510086"
    });

	
    /**
     * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     * NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE!
     * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     *
     *   DO NOT CALL THIS METHOD FROM YOUR JS CODE.
     *
     * This is called by the BigBlueButton Flash client.
     *
     * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
     * NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE! NOTE!
     * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=     
     */
    BBBLog.debug = function (text) {
	  JL('client').debug(JSON.stringify(text));
    }

	BBBLog.info = function (text) {
      JL('client').info(JSON.stringify(text));
    }
    
    BBBLog.error = function (text) {
      JL('client').error(JSON.stringify(text));
    }
        
    BBBLog.warn = function (text) {
      JL('client').warn(JSON.stringify(text));
    }
	
    window.BBBLog = BBBLog;
})(this);

