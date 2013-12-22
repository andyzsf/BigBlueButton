package org.bigbluebutton.apps.protocol

trait UsersMessageFixtures {

    
  val registerUserRequestMessage = """
	{
	    "header": {
            "event" : {
    	        "name": "RegisterUserRequest",
	            "timestamp": 123456,
	            "correlation": "123abc",
	            "source": "web-api"
            },
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101",
	            "sessionId": "english_101-12345"
	        }
	    },
	    "payload": {
	        "user": {
	            "externalId": "user1",
	            "name": "Guga",
	            "role": "MODERATOR",
	            "pin": 12345,
	            "welcomeMessage": "Welcome to English 101",
	            "logoutUrl": "http://www.example.com",
	            "avatarUrl": "http://www.example.com/avatar.png"
	        }
	    }
	} 
  """  
  
  val registerUserReplyMessage = """
	{
	    "header": {
            "event" : {
    	        "name": "RegisterUserReply",
	            "timestamp": 123456,
	            "correlation": "123abc",
	            "source": "web-api"
            },
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101",
	            "sessionId": "english_101-12345"
	        }
	    },
	    "payload": {
	        "user": {
                "token": "user1Token",
                "id": "userid1",
	            "externalId": "user1",
	            "name": "Guga",
	            "role": "MODERATOR",
	            "pin": 12345,
	            "welcomeMessage": "Welcome to English 101",
	            "logoutUrl": "http://www.example.com",
	            "avatarUrl": "http://www.example.com/avatar.png"
	        }
	    }
	} 
  """  
  
    
     
}