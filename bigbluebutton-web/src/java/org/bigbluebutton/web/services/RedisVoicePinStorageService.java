/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
* 
* Copyright (c) 2012 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 3.0 of the License, or (at your option) any later
* version.
* 
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
*
*/

package org.bigbluebutton.web.services;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.bigbluebutton.api.MeetingService;
import org.bigbluebutton.api.messaging.messages.CreateMeeting;
import org.bigbluebutton.api.messaging.messages.EndMeeting;
import org.bigbluebutton.api.messaging.messages.IMessage;
import org.bigbluebutton.api.messaging.messages.MeetingDestroyed;
import org.bigbluebutton.api.messaging.messages.MeetingEnded;
import org.bigbluebutton.api.messaging.messages.MeetingStarted;
import org.bigbluebutton.api.messaging.messages.RegisterPin;
import org.bigbluebutton.api.messaging.messages.RegisterUser;
import org.bigbluebutton.api.messaging.messages.RemoveExpiredMeetings;
import org.bigbluebutton.api.messaging.messages.UserJoined;
import org.bigbluebutton.api.messaging.messages.UserLeft;
import org.bigbluebutton.api.messaging.messages.UserStatusChanged;
import org.bigbluebutton.web.services.messages.DeletePinsMessage;
import org.bigbluebutton.web.services.messages.IVoiceMessage;
import org.bigbluebutton.web.services.messages.StorePinMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisVoicePinStorageService implements IVoicePinStorageService{
	private static Logger log = LoggerFactory.getLogger(RedisVoicePinStorageService.class);
	
	private BlockingQueue<IVoiceMessage> receivedMessages = new LinkedBlockingQueue<IVoiceMessage>();	
	private volatile boolean processMessage = false;
	
	private final Executor msgProcessorExec = Executors.newSingleThreadExecutor();
	private final Executor runExec = Executors.newSingleThreadExecutor();
	
	JedisPool jedisPool;

	/* Meeting Patterns */
	// bbb:meeting:<id>:voicepins [1,2,3] <-- list
	private final String MEETING_PINS = "bbb:meeting:voicepins:";
	// bbb:voicepin:<pin> <-- hash
	private final String VOICE_PIN = "bbb:voicepin:";
	
	private final String VOICE_USERS = "bbb:voice:users:";
	private final String VOICE_USER = "bbb:voice:user:";

  private void handle(IVoiceMessage message) {
		receivedMessages.add(message);    
  }
  
	public void start() {
		log.info("Starting RedisVoicePinStorageService Service.");
		try {
			processMessage = true;
			Runnable messageReceiver = new Runnable() {
			    public void run() {
			    	while (processMessage) {
			    		try {
			    			IVoiceMessage msg = receivedMessages.take();
								processMessage(msg); 			    			
			    		} catch (InterruptedException e) {
			    		  // TODO Auto-generated catch block
			    		  e.printStackTrace();
			    	  } catch (Exception e) {
			    	  	log.error("Handling unexpected exception [{}]", e.toString());
			    	  }
			    	}
			    }
			};
			
			msgProcessorExec.execute(messageReceiver);
		} catch (Exception e) {
			log.error("Error PRocessing Message");
		}
	}
	
	public void stop() {
		processMessage = false;
	}
	
	private void processMessage(final IVoiceMessage message) {
		Runnable task = new Runnable() {
	    public void run() {
	  		if (message instanceof StorePinMessage) {
	  			handleStorePinMessage((StorePinMessage) message);
	  		} else if (message instanceof DeletePinsMessage) {
	  			handleDeletePins((DeletePinsMessage) message);
	  		}
	    }
		};
		
		runExec.execute(task);
	}
	
	private void handleStorePinMessage(StorePinMessage message) {
		Map<String, String> data = new HashMap<String, String>();
		data.put("meetingId", message.meetingId);
		data.put("dialNumber", message.dialNumber);
		data.put("voiceConf", message.voiceConf);
		data.put("pin", message.pin);
		data.put("userId", message.userId);
		data.put("externalUserId", message.externalUserId);
		data.put("username", message.username);
		data.put("role", message.role);
		
		Jedis jedis = (Jedis) jedisPool.getResource();
		try {
			jedis.hmset(VOICE_PIN + message.pin, data);
			jedis.sadd(MEETING_PINS + message.meetingId, message.pin);	
			jedis.hmset(VOICE_USER + message.voiceConf + ":" + message.externalUserId, data);
			jedis.sadd(VOICE_USERS + message.voiceConf, message.externalUserId);
		} finally {
			jedisPool.returnResource(jedis);			
		}		
	}
	
	private void handleDeletePins(DeletePinsMessage message) {
		Jedis jedis = (Jedis) jedisPool.getResource();
		try {
			Set<String> pins = jedis.smembers(MEETING_PINS + message.meetingId);
			for (String pin: pins) {
				jedis.del(VOICE_PIN + pin);
			}
			Set<String> users = jedis.smembers(VOICE_USERS + message.voiceConf);
			for (String user: users) {
				jedis.del(VOICE_USER + message.voiceConf + ":" + user);
			}
			jedis.del(MEETING_PINS + message.meetingId);
		} finally {
			jedisPool.returnResource(jedis);			
		}		
	}
	
	public void storePin(String meetingId, String dialNumber, String voiceConf, String pin, String userId, String externalUserId, String username, String role) {		
    handle(new StorePinMessage(meetingId, dialNumber, voiceConf, pin, userId, externalUserId, username, role));
	}
	
	public void deletePins(String meetingId, String voiceConf) {
		handle(new DeletePinsMessage(meetingId, voiceConf));	
	}

	public void setRedisPool(JedisPool jedisPool){
		this.jedisPool = jedisPool;
	}
}