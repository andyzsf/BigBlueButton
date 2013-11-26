package org.bigbluebutton.apps.protocol

trait ExampleMessageFixtures {
  val invalidJSON = """{ "invalid"  "Missing a colon" }"""
    
  val validJSON = """{ "valid" : "Valid JSON" }"""
  
  val invalidMessage = """ 
{
    "header": {
        "name": "PrivateChatMessageEvent",
        "timestamp": 123456
    },
    "payload1": {
        "valid": "Valid JSON"
    }
}       
    """
    
  val validMessage = """
	{
	    "header": {
	        "event": {
	            "name": "CreateMeetingRequest",
	            "timestamp": 123456,
	            "correlation": "123abc",
	            "source": "web-api"
	        },
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101"
	        }
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101",
	            "record": true,
	            "welcomeMessage": "Welcome to English 101",
	            "logoutUrl": "http://www.bigbluebutton.org",
	            "avatarUrl": "http://www.gravatar.com/bigbluebutton",
	            "users": {
	                "max": 20,
	                "hardLimit": false
	            },
	            "duration": {
	                "length": 120,
	                "allowExtend": false,
	                "warnBefore": 30
	            },
	            "voiceConf": {
	                "pin": 123456,
	                "number": 85115
	            },
	            "phoneNumbers": [
	                {
	                    "number": "613-520-7600",
	                    "description": "Ottawa"
	                },
	                {
	                    "number": "1-888-555-7890",
	                    "description": "NA Toll-Free"
	                }
	            ],
	            "metadata": {
	                "customerId": "acme-customer",
	                "customerName": "ACME"
	            }
	        }
	    }
	}
  """
    
  val createMeetingRequest = """
	{
	    "header": {
	        "event": {
	            "name": "CreateMeetingRequest",
	            "timestamp": 123456,
	            "correlation": "123abc",
	            "source": "web-api"
	        },
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101"
	        }
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101",
	            "record": true,
	            "welcomeMessage": "Welcome to English 101",
	            "logoutUrl": "http://www.bigbluebutton.org",
	            "avatarUrl": "http://www.gravatar.com/bigbluebutton",
	            "users": {
	                "max": 20,
	                "hardLimit": false
	            },
	            "duration": {
	                "length": 120,
	                "allowExtend": false,
	                "warnBefore": 30
	            },
	            "voiceConf": {
	                "pin": 123456,
	                "number": 85115
	            },
	            "phoneNumbers": [
	                {
	                    "number": "613-520-7600",
	                    "description": "Ottawa"
	                },
	                {
	                    "number": "1-888-555-7890",
	                    "description": "NA Toll-Free"
	                }
	            ],
	            "metadata": {
	                "customerId": "acme-customer",
	                "customerName": "ACME"
	            }
	        }
	    }
	} 
    """

  val invalidCreateMeetingRequest = """
	{
	    "header": {
	        "name": "CreateMeeting",
	        "timestamp": 123456,
	        "correlation": "123abc",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting": {
	            "name": "English 101",
	            "externalId": "english_101",
	            "record": true,
	            "welcomeMessage": "Welcome to English 101",
	            "logoutUrl": "http://www.bigbluebutton.org",
	            "avatarUrl": "http://www.gravatar.com/bigbluebutton",
	            "users": {
	                "max": 20,
	                "hardLimit": false
	            },
	            "duration": {
	                "length": 120,
	                "allowExtend": false,
	                "warnBefore": 30
	            },
	            "voiceConf": {
	                "pin": 123456,
	                "number": 85115
	            },
	            "phoneNumbers": [
	                {
	                    "number": "613-520-7600",
	                    "description": "Ottawa"
	                },
	                {
	                    "number": "1-888-555-7890",
	                    "description": "NA Toll-Free"
	                }
	            ],
	            "metadata": [
	               {"customerId": "acme-customer"},
	               {"customerName": "ACME"}
	            ]
	        }
	    }
	}  
    """
    
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
    
}