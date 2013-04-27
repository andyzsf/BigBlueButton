package org.bigbluebutton.transcoder;

import java.io.IOException;

import org.bigbluebutton.transcoder.listener.TranscoderListener;

/**
 * Bootstrap of transcoder server.
 * 
 * BigBlueButton Project.
 * 
 * @author Tiago Daniel Jacobs <os@tdj.cc>
 * */
public class Bootstrap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TranscoderListener transcoderListener = new TranscoderListener();
			transcoderListener.listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
