package org.bigbluebutton.endpoint.redis

import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.AppsTestFixtures


trait UsersMessageTestFixtures extends AppsTestFixtures {
  val userJoinedMessage = UserJoined(eng101Session, joinedUserJuan.token, joinedUserJuan)
   
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
    
  val userLeaveMsg = """
	{
	    "header": {
	        "event": {
	            "name": "user_leave",
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
	        "user_id": "user1"
	    }
	}    
  """
    
  val getUsersMsg = """
	{
	    "header": {
	        "event": {
	            "name": "get_users",
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
	        "requester_id": "user1"
	    }
	}     
  """
    
  val assignPresenterMsg = """
	{
	    "header": {
	        "event": {
	            "name": "assign_presenter",
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
	        "presenter": {
	            "id": "user1",
	            "name": "Guga"
	        },
	        "assigned_by": {
	            "id": "user2",
	            "name": "Juan"
	        }
	    }
	}    
    
  """
  
  
}