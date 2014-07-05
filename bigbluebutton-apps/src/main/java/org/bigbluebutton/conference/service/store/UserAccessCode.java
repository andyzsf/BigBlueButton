package org.bigbluebutton.conference.service.store;

import java.util.HashMap;

public class UserAccessCode implements DataToStore {

	public final String key;
	public final HashMap<String, String> map;
	
	public UserAccessCode(String key, HashMap<String, String> map) {
		this.key = key;
		this.map = map;
	}
}
