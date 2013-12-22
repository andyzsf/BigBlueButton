package org.bigbluebutton.endpoint.redis

trait UsersMessageTestFixtures {

  val userJoinMsg = """
	{
	    "name": "user_join",
	    "timestamp": 123456,
	    "meeting": {
	        "id": "english_101",
	        "name": "English 101",
	        "session": "183f0bf3a0982a127bdb8161e0c44eb696b3e75c-1383210136298"
	    },
	    "payload": {
	        "token": "user1-token-1"
	    }
	}    
  """
}