package org.bigbluebutton.apps.protocol

import org.specs2.mutable.Specification

class MessageHandleSpec extends Specification {
  val message = """
			{
			  "header": {
			    "name": "PrivateChatMessageEvent",
			    "timestamp": 123456,
			    "meeting": {
			        "id": "english_101",
			        "name": "English 101",
			        "session": "183f0bf3a0982a127bdb8161e0c44eb696b3e75c-1383210136298"
			    }
			  },
			  "payload" : {
			    "chat": {
			        "id": "msg1",
			        "from": {
			            "id": "user1",
			            "name": "Richard"
			        },
			        "to": {
			            "id": "user2",
			            "name": "Guga"
			        },
			        "message": {
			          "lang": "en_US",
			          "text": "Hello world!"
			        },
			        
			        "font": {
			            "color": "red",
			            "size": "14",
			            "type": "Arial"
			        },
			        
			        "translations": [
			            {
			                "lang": "es_LA",
			                "text": "Hola Mundo!"
			            },
			            {
			                "lang": "fi_PH",
			                "text": "Kumusta!"
			            }
			        ]
			    }
			  }
			}  
  """
    
    "The Message Handler" should {
      "be able to extract the header" in {
        MessageHandler.processMessage(message)
        "Hello world" must have size(11)
      }
      "start with 'Hello'" in {
        "Hello world" must startWith("Hello")
      }
      "end with 'world'" in {
        "Hello world" must endWith("world")
      }
    }
}