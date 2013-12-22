package org.bigbluebutton.endpoint.redis

trait UsersMessageTestFixtures {

  val userJoinMsg = """
	{
	    "header": {
	        "event": {
	            "name": "user_join",
	            "timestamp": 123456,
	            "source": "web-api"
	        },
	        "meeting": {
	            "name": "English 101",
	            "id": "english_101",
	            "session": "english_101-12345"
	        }
	    },
	    "payload": {
	        "token": "user1-token-1"
	    }
	}    
  """
}