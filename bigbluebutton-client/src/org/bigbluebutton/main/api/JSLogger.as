package org.bigbluebutton.main.api
{
  import flash.external.ExternalInterface;

  class JSLogger {
    private static var instance:JSLogger = null;
    
    public function JSLogger(enforcer:JSLoggerSingletonEnforcer) {
      if (enforcer == null) {
        throw new Error("There can only be 1 JSLogger instance");
      }
    }
    
    public static function getInst():JSLogger {
      if (instance == null){
        instance = new JSLogger(new JSLoggerSingletonEnforcer());
      }
      return instance;
    }
    
    public function debug(text:Object):void
    {
      ExternalInterface.call("BBBLog.debug", text);
    }
    
    public function info(text:Object):void
    {
      ExternalInterface.call("BBBLog.info", text);
    }
    
    public function error(text:Object):void
    {
      ExternalInterface.call("BBBLog.error", text);
    }
        
    public function warn(text:Object):void
    {
      ExternalInterface.call("BBBLog.warn", text);
    }
       
  }
}

class JSLoggerSingletonEnforcer{}