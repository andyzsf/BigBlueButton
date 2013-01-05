package org.bigbluebutton.main.api
{
	import flash.external.ExternalInterface;
	
	public class ConnAPI {
		public static function connected():void {
			if (ExternalInterface.available) {
				ExternalInterface.call("BBBConn.connected");
			}		
	
		}		
	}

}