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

package org.bigbluebutton.api.messaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import org.bigbluebutton.api.messaging.converters.messages.CreateMeetingMessage;
import org.bigbluebutton.api.messaging.converters.messages.DestroyMeetingMessage;
import org.bigbluebutton.api.messaging.converters.messages.EndMeetingMessage;
import org.bigbluebutton.api.messaging.converters.messages.KeepAliveMessage;
import org.bigbluebutton.api.messaging.converters.messages.RegisterUserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class RedisMessagingService implements MessagingService {
	private static Logger log = LoggerFactory.getLogger(RedisMessagingService.class);
	
	private RedisStorageService storeService;
	private MessageSender sender;
	
	public void recordMeetingInfo(String meetingId, Map<String, String> info) {
		storeService.recordMeetingInfo(meetingId, info);	
	}

	public void destroyMeeting(String meetingID) {
		DestroyMeetingMessage msg = new DestroyMeetingMessage(meetingID);
		String json = MessageToJson.destroyMeetingMessageToJson(msg);
		log.info("Sending destory meeting message to bbb-apps:[{}]", json);
		sender.send(MessagingConstants.TO_MEETING_CHANNEL, json);	
	}
	
	public void registerUser(String meetingID, String internalUserId, String fullname, String role, String externUserID, String authToken) {
		RegisterUserMessage msg = new RegisterUserMessage(meetingID, internalUserId, fullname, role, externUserID, authToken);
		String json = MessageToJson.registerUserToJson(msg);
		log.info("Sending register user message to bbb-apps:[{}]", json);
		sender.send(MessagingConstants.TO_MEETING_CHANNEL, json);		
	}
	
	public void createMeeting(String meetingID, String meetingName, Boolean recorded, String voiceBridge, Long duration) {
		CreateMeetingMessage msg = new CreateMeetingMessage(meetingID, meetingName, recorded, voiceBridge, duration);
		String json = MessageToJson.createMeetingMessageToJson(msg);
		log.info("Sending create meeting message to bbb-apps:[{}]", json);
		sender.send(MessagingConstants.TO_MEETING_CHANNEL, json);			
	}
	
	public void endMeeting(String meetingId) {
		EndMeetingMessage msg = new EndMeetingMessage(meetingId);
		String json = MessageToJson.endMeetingMessageToJson(msg);
		log.info("Sending end meeting message to bbb-apps:[{}]", json);
		sender.send(MessagingConstants.TO_MEETING_CHANNEL, json);	
	}

  public void sendKeepAlive(String keepAliveId) {
		KeepAliveMessage msg = new KeepAliveMessage(keepAliveId);
		String json = MessageToJson.keepAliveMessageToJson(msg);
		sender.send(MessagingConstants.TO_SYSTEM_CHANNEL, json);		
  }
	
  public void send(String channel, String message) {
		sender.send(channel, message);
  }
  
	public void sendPolls(String meetingId, String title, String question, String questionType, List<String> answers){
		Gson gson = new Gson();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("messageId", MessagingConstants.SEND_POLLS_EVENT);
		map.put("meetingId", meetingId);
		map.put("title", title);
		map.put("question", question);
		map.put("questionType", questionType);
		map.put("answers", answers);
		
		System.out.println(gson.toJson(map));
		
		sender.send(MessagingConstants.TO_POLLING_CHANNEL, gson.toJson(map));		
	}

	public void setMessageSender(MessageSender sender) {
		this.sender = sender;
	}
	
  public void setRedisStorageService(RedisStorageService storeService) {
  	this.storeService = storeService;
  }
  
	public String storeSubscription(String meetingId, String externalMeetingID, String callbackURL){
		return storeService.storeSubscription(meetingId, externalMeetingID, callbackURL);
	}

	public boolean removeSubscription(String meetingId, String subscriptionId){
		return storeService.removeSubscription(meetingId, subscriptionId);
	}

	public List<Map<String,String>> listSubscriptions(String meetingId){
		return storeService.listSubscriptions(meetingId);	
	}	
	
	public void setRedisPool(JedisPool redisPool){
		this.redisPool=redisPool;
	}
	
	private class PubSubListener extends JedisPubSub {
		
		public PubSubListener() {
			super();			
		}

		@Override
		public void onMessage(String channel, String message) {
			// Not used.
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			log.debug("Message Received in channel: " + channel);
			log.debug("Message: " + message);
			
			Gson gson = new Gson();
			
//			for (String key: map.keySet()) {
//				log.debug("rx: {} = {}", key, map.get(key));
//			}
			
			if(channel.equalsIgnoreCase(MessagingConstants.SYSTEM_CHANNEL)){
				HashMap<String,String> map = gson.fromJson(message, new TypeToken<Map<String, String>>() {}.getType());
				String messageId = map.get("messageID");
				log.debug("*** Message {}", messageId);

				for (MessageListener listener : listeners) {
					if(MessagingConstants.MEETING_STARTED_EVENT.equalsIgnoreCase(messageId)) {
						String meetingId = map.get("meetingID");
						listener.meetingStarted(meetingId);
					} else if(MessagingConstants.MEETING_ENDED_EVENT.equalsIgnoreCase(messageId)) {
						String meetingId = map.get("meetingID");
						listener.meetingEnded(meetingId);
					} else if(MessagingConstants.KEEP_ALIVE_REPLY_EVENT.equalsIgnoreCase(messageId)){
						String aliveId = map.get("aliveID");
						listener.keepAliveReply(aliveId);
					}
				}
			}
			else if(channel.equalsIgnoreCase(MessagingConstants.PARTICIPANTS_CHANNEL)){
				HashMap<String,String> map = gson.fromJson(message, new TypeToken<Map<String, String>>() {}.getType());
				String meetingId = map.get("meetingID");
				String messageId = map.get("messageID");
				if(MessagingConstants.USER_JOINED_EVENT.equalsIgnoreCase(messageId)){
					String internalUserId = map.get("internalUserID");
					String externalUserId = map.get("externalUserID");
					String fullname = map.get("fullname");
					String role = map.get("role");
					
					for (MessageListener listener : listeners) {
						listener.userJoined(meetingId, internalUserId, externalUserId, fullname, role);
					}
				} else if(MessagingConstants.USER_STATUS_CHANGE_EVENT.equalsIgnoreCase(messageId)){
					String internalUserId = map.get("internalUserID");
					String status = map.get("status");
					String value = map.get("value");
					
					for (MessageListener listener : listeners) {
						listener.updatedStatus(meetingId, internalUserId, status, value);
					}
				} else if(MessagingConstants.USER_LEFT_EVENT.equalsIgnoreCase(messageId)){
					String internalUserId = map.get("internalUserID");
					
					for (MessageListener listener : listeners) {
						listener.userLeft(meetingId, internalUserId);
					}
				}
			}
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			log.debug("Subscribed to the pattern:"+pattern);
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			// Not used.
		}

		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			// Not used.
		}

		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			// Not used.
		}		
	}

	public void recordMeeting(String meetingID, String externalID, String name) {
		Jedis jedis = redisPool.getResource();
		try {
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("meetingID", meetingID);
			map.put("externalID", externalID);
			map.put("name", name);
			
			jedis.hmset("meeting-" + meetingID, map);
			jedis.sadd("meetings", meetingID);

		} finally {
			redisPool.returnResource(jedis);
		}
	}
	
	public void removeMeeting(String meetingId){
		Jedis jedis = redisPool.getResource();
		try {
			jedis.del("meeting-" + meetingId);
			//jedis.hmset("meeting"+ COLON +"info" + COLON + meetingId, metadata);
			jedis.srem("meetings", meetingId);

		} finally {
			redisPool.returnResource(jedis);
		}
	}

	public void recordPresentation(String meetingID, String presentationName, int numberOfPages) {
		Jedis jedis = redisPool.getResource();
		try {
			
			jedis.sadd("meeting-" + meetingID + "-presentations", presentationName);
			for(int i=1;i<=numberOfPages;i++){
				jedis.rpush("meeting-"+meetingID+"-presentation-"+presentationName+"-pages", Integer.toString(i));
				jedis.set("meeting-"+meetingID+"-presentation-"+presentationName+"-page-"+i+"-image", "slide"+i+".png");
				
				/*default image size is 800x600.
				This will cause images in any other aspect ratio to be stretched out
				To fix this, we pass the image size through redis which is obtained from the png files in the 
				var/bigbluebutton directory*/
				int width = 800;
				int height = 600;
				try {
					BufferedImage bimg = ImageIO.read(new File("/var/bigbluebutton/"+meetingID+"/"+meetingID+"/"+presentationName+"/pngs/slide"+i+".png"));
					width = bimg.getWidth();
					height = bimg.getHeight();
				} catch (IOException e) {
					/*If there is an error in opening the files, the images will default back to 800x600
					This is not ideal*/
					width = 800;
					height = 600;
					log.error(e.toString());
				}
				
				jedis.set("meeting-"+meetingID+"-presentation-"+presentationName+"-page-"+i+"-width", Integer.toString(width));
				jedis.set("meeting-"+meetingID+"-presentation-"+presentationName+"-page-"+i+"-height", Integer.toString(height));
			}
			jedis.set("meeting-" + meetingID + "-currentpresentation", presentationName);
			//VIEWBOX
			ArrayList viewbox = new ArrayList();
			viewbox.add(0);
			viewbox.add(0);
			viewbox.add(1);
			viewbox.add(1);
			Gson gson = new Gson();
			jedis.set("meeting-" + meetingID + "-viewbox", gson.toJson(viewbox));
		} finally {
			redisPool.returnResource(jedis);
		}
        }

	public void removeMeeting(String meetingId){
		storeService.removeMeeting(meetingId);
	}
	
}
