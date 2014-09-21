package org.bigbluebutton.main.api
{
  import org.bigbluebutton.core.UsersUtil;
  import org.bigbluebutton.core.connection.messages.UserUnsharedWebcamMessage;

  public class JSLog
  {
    public static const LOGGER:String = "BBBLOGGER";
    
    public static function logObject():Object {
      var obj:Object = new Object();
      obj.userId = UsersUtil.getMyUserID();
      obj.meetingId = UsersUtil.getInternalMeetingID();
      
      return obj;
    }
    
    public static function debug(message:Object):void
    {
      logger.debug(message);
    }
    
    public static function info(message:Object):void
    {
      logger.info(message);
    }
    
    public static function error(message:Object):void
    {
      logger.error(message);
    }
    
    public static function warn(message:Object):void
    {
      logger.warn(message);
    }
        
    private static function get logger():JSLogger {
      return JSLogger.getInst();
    }
  }
}