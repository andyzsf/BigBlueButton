package org.bigbluebutton.transcoder.listener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.bigbluebutton.transcoder.protocol.worker.BTPWorker;

/**
 * TCP Listener that receive requests from the bbb-video.
 * 
 * BigBlueButton Project.
 * 
 * @author Tiago Daniel Jacobs <os@tdj.cc>
 * */
public class TranscoderListener {

	private final ServerSocket _socketServer;
	private final Integer tcpBackLog = 3000;
	private final Integer tcpPort = 8101;
	
	/**
	 * @throws IOException 
	 * 
	 * */
	public TranscoderListener() throws IOException {
		_socketServer = new ServerSocket(tcpPort, tcpBackLog);
		_socketServer.setReuseAddress(true);
	}
	
	/**
	 * @throws IOException 
	 * 
	 * */
	public void listen() throws IOException {
		Socket newSocket = null;
		
		while( (newSocket = _socketServer.accept()) != null) {
			Thread socketThread = new Thread(new BTPWorker(newSocket));
			socketThread.run();
		}
	}

}
