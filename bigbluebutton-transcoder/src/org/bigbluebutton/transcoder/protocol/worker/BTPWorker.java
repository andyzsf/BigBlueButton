package org.bigbluebutton.transcoder.protocol.worker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.bigbluebutton.transcoder.protocol.BTP;

/**
 * BigBlueButton Transcoder Protocol
 * 
 * @author Tiago Daniel Jacobs <os@tdj.cc>
 * */
public class BTPWorker implements Runnable {
	Socket _socket;
	BufferedReader _reader;
	BufferedWriter _writer;
	Boolean _disconnectionExpected = false;
	
	public BTPWorker(Socket socket) {
		_socket = socket;
	}
	
	@Override
	public void run() {
		BufferedInputStream is;
		BufferedOutputStream os;
		try {
			is = new BufferedInputStream (_socket.getInputStream());
			os = new BufferedOutputStream(_socket.getOutputStream());
		} catch (IOException e) {
			System.err.println("Error getting input stream: " + e.getMessage());
			
			try {
				_socket.close();
			} catch (IOException e1) {}
			
			_socket = null;
			return;
		}
		
		_reader = new BufferedReader(new InputStreamReader(is));
		_writer = new BufferedWriter(new OutputStreamWriter(os));
		
		String receivedLine;
		Boolean handshakeReceived = false;
		
		try {
			while((receivedLine = _reader.readLine()) != null) {
				//We expect a handshake
				if(!handshakeReceived) {
					receivedLine = receivedLine.trim();
					
					if(receivedLine.equals(BTP.HANDSHAKE)) {
						handshakeReceived = true;
						_writer.write(BTP.HANDSHAKE);
						_writer.write(BTP.COMMAND_SEPARATOR);
						_writer.flush();
					} else {
						System.err.println("Unexpected data received.");
						_socket.close();
						_socket = null;
						return;
					}
				} else {
					processCommand(receivedLine);
				}
			}
		} catch (IOException e) {
			if(!_disconnectionExpected) {
				System.err.println("Client unexpectedly disconnected: " + e.getMessage());
			}
			_socket = null;
		}
	}

	private void processCommand(String receivedLine) {
		if(receivedLine.startsWith(BTP.COMMAND_EXIT)) {
			try {
				_disconnectionExpected = true;
				_socket.close();
				_socket = null;
			} catch (IOException e) {}
		}
	}
	
}
