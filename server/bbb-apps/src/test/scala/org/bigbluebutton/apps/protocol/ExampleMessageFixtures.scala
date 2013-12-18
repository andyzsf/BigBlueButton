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
	            "reply": {"to": "replyChannel", "correlationId" : "abc123"},
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
	            "reply": {"to": "replyChannel", "correlationId" : "abc123"},
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
  
  val exampleChatMessage = """
	{
	    "header": {
	        "event": {
	            "name": "ChatMessage",
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
	        "chat": {
	            "id": "msg1",
	            "sentOn": 1383210123456,
	            "from": {
	                "id": "user1",
	                "name": "Richard"
	            },
	            "message": {
	                "text": "Hello world!",
	                "lang": "en_US"
	            },
	            "font": {
	                "color": 16711680,
	                "size": 14,
	                "type": "Arial"
	            },
	            "translations": [
	                {
	                    "lang": "es_LA",
	                    "text": "Hola Mundo!"
	                }
	            ]
	        }
	    }
	}    
  """
    
  val wbmsg = """
{
    "name": "WhiteboardDraw",
    "timestamp": 123456,
    "meeting": {
        "id": "english_101",
        "name": "English 101",
        "sessionID": "183f0bf3a0982a127bdb8161e0c44eb696b3e75c-1383210136298"
    },
    "shape": {
        "id": "user1-shape-1",
        "correlationId": "q779ogycfmxk-13-1383262166102",
        "type": "text",
        "data": {
            "coordinate": {
                "firstX": 0.016025641025641028,
                "firstY": 0.982905982905983,
                "lastX": 1.33,
                "lastY": 2.45
            },
            "font": {
                "color": 0,
                "size": 18
            },
            "background": true,
            "backgroundColor": 16777215,
            "text": "He"
        },
        "by": {
            "id": "user1",
            "name": "Guga"
        }
    }
}
  """    
}