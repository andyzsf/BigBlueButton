package org.bigbluebutton;

import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Ready;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.cpr.*;
import org.atmosphere.config.service.Singleton;
import org.bigbluebutton.AppsProxy;

@Singleton
@ManagedService(path = "/search")
public class AppsProxyManagedService {
	private AppsProxy s = new AppsProxy();
	
    @Ready
    public void onReady(AtmosphereResource r) {
       System.out.println("Browser has connected. [" + r.uuid() + "]");
       s.onReady(r);
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
      System.out.println("Browser has disconnected! [" + event.getResource().uuid());
      s.onDisconnect(event);
    }

    @Message
    public void onMessage(AtmosphereResource r, String m) {
    	System.out.println("Received message [" + m + "] from [" + r.uuid());
    	s.onMessage(r, m);
    }
}
