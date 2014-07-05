package org.bigbluebutton.conference.service.store;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisStore {
	private static Logger log = Red5LoggerFactory.getLogger(RedisStore.class, "bigbluebutton");
	private JedisPool redisPool;
	private volatile boolean storeMessage = false;
	
	private final Executor msgStoreExec = Executors.newSingleThreadExecutor();
	private BlockingQueue<DataToStore> messages = new LinkedBlockingQueue<DataToStore>();
	
	public void stop() {
		storeMessage = false;
	}
	
	public void start() {	
		log.info("Redis message publisher starting!");
		try {
			storeMessage = true;
			
			Runnable messageSender = new Runnable() {
			    public void run() {
			    	while (storeMessage) {
				    	try {
				    		DataToStore data = messages.take();
							  store(data);
						} catch (InterruptedException e) {
							log.warn("Failed to get message from queue.");
						}    			    		
			    	}
			    }
			};
			msgStoreExec.execute(messageSender);
		} catch (Exception e) {
			log.error("Error subscribing to channels: " + e.getMessage());
		}			
	}
	
	private void store(DataToStore data) {
		if (data instanceof UserAccessCode) {
			UserAccessCode ds = (UserAccessCode) data;
			String key = ds.key;
			Jedis jedis = redisPool.getResource();
			try {
				jedis.hmset(key, ds.map);
			} catch(Exception e){
				log.warn("Cannot store to redis", e);
			} finally {
				redisPool.returnResource(jedis);
			}						
		}
	}
	
	public void addUserAccessCode(String accessCode, String userId, String meetingId, String voiceConf, String role, String did) {
		String key = "voice:user:access:code:" + accessCode;
		HashMap<String, String> map = new HashMap<String, String>();
		
		UserAccessCode ds = new UserAccessCode(key, map);		
		messages.add(ds);
	}
	
	public void setRedisPool(JedisPool redisPool){
		this.redisPool = redisPool;
	}
}
