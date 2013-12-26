package org.bigbluebutton.endpoint

trait JsonMessagesFixtures {
  
  val CreateMeetingRequestJson     = 
"""
	{
	    "header": {
	        "destination": {
	            "to": "apps_channel"
	        },
	        "reply": {
	            "to": "apps_channel",
	            "correlation_id": "abc"
	        },
	        "name": "create_meeting_request",
	        "timestamp": "2013-12-23T08:50Z",
	        "source": "web-api"
	    },
	    "payload": {
	        "meeting_descriptor": {
	            "name": "English 101",
	            "external_id": "english_101",
	            "record": true,
	            "welcome_message": "Welcome to English 101",
	            "logout_url": "http://www.bigbluebutton.org",
	            "avatar_url": "http://www.gravatar.com/bigbluebutton",
	            "max_users": 20,
	            "duration": {
	                "length": 120,
	                "allow_extend": false,
	                "max": 240
	            },
	            "voice_conference": {
	                "pin": 123456,
	                "number": 85115
	            },
	            "phone_numbers": [
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
	                "customer_id": "acme-customer",
	                "customer_name": "ACME"
	            }
	        }
	    }
	}     
"""
    
  val CreateMeetingResponseJson    = 
"""
{
    "header": {
        "destination": {
            "to": "apps_channel",
            "correlation_id": "abc"
        },
        "name": "create_meeting_response",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-1234",
        "result": {
            "success": true,
            "message": "Success"
        },
        "meeting_descriptor": {
            "name": "English 101",
            "external_id": "english_101",
            "record": true,
            "welcome_message": "Welcome to English 101",
            "logout_url": "http://www.bigbluebutton.org",
            "avatar_url": "http://www.gravatar.com/bigbluebutton",
            "max_users": 20,
            "duration": {
                "length": 120,
                "allow_extend": false,
                "max": 240
            },
            "voice_conference": {
                "pin": 123456,
                "number": 85115
            },
            "phone_numbers": [
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
                "customer_id": "acme-customer",
                "customer_name": "ACME"
            }
        }
    }
}
"""
    
  val MeetingCreatedEvent      = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "meeting_created_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "meeting_descriptor": {
            "name": "English 101",
            "external_id": "english_101",
            "record": true,
            "welcome_message": "Welcome to English 101",
            "logout_url": "http://www.bigbluebutton.org",
            "avatar_url": "http://www.gravatar.com/bigbluebutton",
            "max_users": 20,
            "duration": {
                "length": 120,
                "allow_extend": false,
                "max": 240
            },
            "voice_conference": {
                "pin": 123456,
                "number": 85115
            },
            "phone_numbers": [
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
                "customer_id": "acme-customer",
                "customer_name": "ACME"
            }
        }
    }
}    
    """
    
  val EndMeetingRequestJson        = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "reply": {
            "to": "apps_channel",
            "correlation_id": "abc"
        },
        "name": "end_meeting_request",
        "timestamp": "2013-12-23T08: 50Z",
        "source": "bbb-web"
    },
    "payload": {
        "meeting": {
            "name": "English101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "force": true,
        "warn_users": true
    }
}    
    """
    
  val EndMeetingResponseJson       = """
{
    "header": {
        "destination": {
            "to": "apps_channel",
            "correlation_id": "abc"
        },
        "name": "end_meeting_response",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "result": {
            "success": true,
            "message": "Success"
        }
    }
}    
    """
    
  val EndMeetingWarningEventJson          = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "end_meeting_warning_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "time_left": 30,
        "time_unit": "seconds",
        "allow_extend": false
    }
}    
    """
    
  val MeetingEndedEventJson        = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "meeting_ended_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345"
    }
}    
    """
    
  val RegisterUserRequestJson      = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "reply": {
            "to": "apps_channel",
            "correlation_id": "abc"
        },
        "name": "register_user_request",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-web"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "user_descriptor": {
            "external_id": "user1",
            "name": "Guga",
            "role": "MODERATOR",
            "pin": 12345,
            "welcome_message": "Welcome to English 101",
            "logout_url": "http://www.example.com",
            "avatar_url": "http://www.example.com/avatar.png"
        }
    }
}    
    """
    
  val RegisterUserResponseJson     = """
{
    "header": {
        "destination": {
            "to": "apps_channel",
            "correlation_id": "abc"
        },
        "name": "register_user_response",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "user_token": "guga-token",
        "result": {
            "success": true,
            "message": "Success"
        },
        "user_descriptor": {
            "external_id": "user1",
            "name": "Guga",
            "role": "MODERATOR",
            "pin": 12345,
            "welcome_message": "Welcome to English 101",
            "logout_url": "http://www.example.com",
            "avatar_url": "http://www.example.com/avatar.png"
        }
    }
}    
    """
    
  val UserRegisteredEventJson      = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_registered_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "bbb-apps"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "user_descriptor": {
            "external_id": "user1",
            "name": "Guga",
            "role": "MODERATOR",
            "pin": 12345,
            "welcome_message": "Welcome to English 101",
            "logout_url": "http://www.example.com",
            "avatar_url": "http://www.example.com/avatar.png"
        }
    }
}    
    """
    
  val UserJoinRequestJson          = """
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
        "source": "bbb-apps"
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
    
  val UserJoinResponseJson         = """
{
    "header": {
        "destination": {
            "to": "apps_channel",
            "correlation_id": "abc-corelid"
        },
        "name": "user_join_response",
        "timestamp": "2013-12-23T08:50Z",
        "source": "web-api"
    },
    "payload": {
        "meeting": {
            "id": "english_101",
            "name": "English 101"
        },
        "session": "english_101-1234",
        "result": {
            "success": true,
            "message": "Success"
        },
        "user": {
            "id": "juan-user1",
            "external_id": "juan-ext-user1",
            "name": "Juan Tamad",
            "role": "MODERATOR",
            "pin": 12345,
            "welcome_message": "Welcome Juan",
            "logout_url": "http://www.umaliska.don",
            "avatar_url": "http://www.mukhamo.com/unggoy"
        }
    }
}    
    """
    
  val UserJoinedEventJson          = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_joined_event",
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
    
  val UserLeaveEventJson           = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_leave_event",
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
            "name": "Juan Tamad"
        }
    }
}    
    """
    
  val UserLeftEventJson            = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_left_event",
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
            "name": "Juan Tamad"
        }
    }
}    
    """
    
  val GetUsersRequestJson          = """
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
        "source": "bbb-web"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "requester": {
            "id": "juanid",
            "name": "Juan Tamad"
        }
    }
}    
    """
    
  val GetUsersResponseJson         = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "get_users_response",
        "timestamp": "2013-12-23T08:50Z",
        "source": "web-api"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "users": [
            {
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
        ]
    }
}    
    """
    
  val RaiseUserHandRequestJson     = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "raise_user_hand_request",
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
            "id": "juanid",
            "name": "Juan Tamad"
        },
        "raise": true
    }
}    
    """
    
  val UserRaisedHandEventJson      = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_raised_hand_event",
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
            "id": "juanid",
            "name": "Juan Tamad"
        },
        "raised": true
    }
}    
    """
    
  val AssignPresenterRequestJson   = """
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
    
  val PresenterAssignedEventJson   = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "presenter_assigned_event",
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
    
  val MuteUserRequestJson          = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "mute_user_request",
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
        },
        "requester": {
            "id": "user2",
            "name": "Juan"
        },
        "mute": true
    }
}    
    """
    
  val MuteUserRequestEventJson     = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "mute_user_request_event",
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
        },
        "requester": {
            "id": "user2",
            "name": "Juan"
        },
        "mute": true
    }
}    
    """

  val MuteVoiceUserRequestJson   = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "mute_voice_user_request",
        "timestamp": "2013-12-23T08:50Z",
        "source": "web-api"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "mute": true,
        "user_metadata": {
            "id": "user1",
            "name": "Guga"
        },
        "voice_metadata": {
            "FreeSWITCH-IPv4": "192.168.0.166",
            "Conference-Name": "72382",
            "Conference-Unique-ID": "480d3f7c-224f-11e0-ae04-fbe97e271da0",
            "conference_member_id": "1"
        }
    }
}    
    """

    val VoiceUserMutedEventJson   = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "voice_user_muted_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "fs-esl"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "muted": true,
        "user_metadata": {
            "id": "user1",
            "name": "Guga"
        },
        "voice_metadata": {
            "FreeSWITCH-IPv4": "192.168.0.166",
            "Conference-Name": "72382",
            "Conference-Unique-ID": "480d3f7c-224f-11e0-ae04-fbe97e271da0",
            "conference_member_id": "1"
        }
    }
}   
    """
        
  val UserMutedEventJson           = """
{
    "header": {
        "destination": {
            "to": "apps_channel"
        },
        "name": "user_muted_event",
        "timestamp": "2013-12-23T08:50Z",
        "source": "fs-esl"
    },
    "payload": {
        "meeting": {
            "name": "English 101",
            "id": "english_101"
        },
        "session": "english_101-12345",
        "muted": true,
        "user": {
            "id": "user1",
            "name": "Guga"
        }
    }
}       
    """
    

}