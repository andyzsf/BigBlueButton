package org.bigbluebutton.endpoint.redis

import org.bigbluebutton.apps.users.data._
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.users.messages.UserJoined
import org.bigbluebutton.apps.AppsTestFixtures
import org.bigbluebutton.apps.users.protocol.UserJoinRequestMessage
import org.bigbluebutton.apps.users.protocol.UserJoinRequestPayload
import org.bigbluebutton.apps.protocol.Destination
import org.bigbluebutton.apps.protocol.Header
import org.bigbluebutton.apps.users.messages.UserJoinResponse
import org.bigbluebutton.apps.users.messages.Result
import org.bigbluebutton.apps.protocol.ReplyDestination


trait UsersMessageTestFixtures extends AppsTestFixtures {
  val userJoinSuccessResponse = UserJoinResponse(eng101Session, Result(true, "Success"),
                              Some(joinedUserJuan))
  val userJoinFailResponse = UserJoinResponse(eng101Session, Result(false, "Success"),
                              None)
                              
  val userJoinRequestPayload = UserJoinRequestPayload(eng101MeetingIdAndName, 
                                  eng101SessionId, juanUserToken)
                                  
  val destination = Destination("apps_channel", None)
  val replyTo = ReplyDestination("apps_channel", "abc-corelid")
  val userJoinedHeader = Header(destination, InMsgNameConst.UserJoinRequest, 
                  "2013-12-23T08:50Z", "web-api",
                  Some(replyTo))
  val userJoinRequestMessage = UserJoinRequestMessage(userJoinedHeader, 
                                  userJoinRequestPayload)
  
  val userJoinMsg = """
	{
	    "header": {
	        "destination": {
	            "to": "apps_channel"
	        },
	        "reply": {
	            "to": "apps_channel",
	            "correlation_id": "abc"
	        },
	        "name": "user_join_request",
	        "timestamp": "2013-12-23T08:50Z",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "id": "english_101"
	        },
	        "session": "english_101-12345",
	        "token": "user1-token-1"
	    }
	}   
  """

  val userJoinedJsonMessage = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_joined",
        "timestamp": "2013-12-23T08:50Z",
        "source": "web-api"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "user": {
            "id": "juanid",
            "external_id": "userjuan",
            "name": "Juan Tamad",
            "role": "MODERATOR",
            "pin": 12345,
            "welcome_message": "Welcome Juan",
            "logout_url": "http://www.umaliska.don",
            "avatar_url": "http://www.mukhamo.com/unggoy",
            "is_presenter": true,
            "status": {
                "hand_raised": false,
                "muted": false,
                "locked": false,
                "talking": false
            },
            "caller_id": {
                "name": "Juan Tamad",
                "number": "011-63-917-555-1234"
            },
            "media_streams": [
                {
                    "media_type": "audio",
                    "uri": "http://cdn.bigbluebutton.org/stream/a1234"
                },
                {
                    "media_type": "video",
                    "uri": "http://cdn.bigbluebutton.org/stream/v1234"
                },
                {
                    "media_type": "screen",
                    "uri": "http://cdn.bigbluebutton.org/stream/s1234"
                }
            ]
        }
    }
}     
  """
    
  val userLeaveMsg = """
	{
	    "header": {
	        "destination": {
	            "to": "apps_channel"
	        },
	        "name": "user_leave_request",
	        "timestamp": "2013-12-23T08:50Z",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "id": "english_101"
	        },
	        "session": "english_101-12345",
	        "user": {
	            "id": "user1",
	            "name": "Guga"
	        }
	    }
	}  
  """
    
  val getUsersMsg = """
	{
	    "header": {
	        "destination": {
	            "to": "apps_channel"
	        },
	        "reply": {
	            "to": "apps_channel",
	            "correlation_id": "abc"
	        },
	        "name": "get_users_request",
	        "timestamp": "2013-12-23T08:50Z",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "id": "english_101"
	        },
	        "session": "english_101-12345",
	        "requester": {
	            "id": "user1",
	            "name": "Guga"
	        }
	    }
	}     
  """
    
  val assignPresenterMsg = """
	{
	    "header": {
	        "destination": {
	            "to": "apps_channel"
	        },
	        "name": "assign_presenter_request",
	        "timestamp": "2013-12-23T08:50Z",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "id": "english_101"
	        },
	        "session": "english_101-12345",
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