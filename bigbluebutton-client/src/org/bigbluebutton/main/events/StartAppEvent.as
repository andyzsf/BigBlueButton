package org.bigbluebutton.main.events
{
  import flash.events.Event;
  
  public class StartAppEvent extends Event
  {
    public static const START_APP_EVENT:String = "start bbb client event";
    
    public function StartAppEvent()
    {
      super(START_APP_EVENT, true, false);
    }
  }
}