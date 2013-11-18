package org.bigbluebutton.apps.protocol

import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.Chat._
import spray.json.JsValue
import spray.json.JsObject
   
object ChatJsonProtocol extends DefaultJsonProtocol {
  implicit val chatterFormat = jsonFormat2(Chatter)
  implicit val chatFontFormat = jsonFormat3(ChatFont)
  implicit val chatTranslationFormat = jsonFormat2(ChatText)
  implicit val privateChatMessageFormat = jsonFormat5(PrivateChatMessage)
}

class ChatJsonProtocol {
	def extractPayload(payload:JsObject):Option[JsValue] = {
	  payload.fields.get("payload") match {
	    case Some(chat) => Some(chat)
	    case None => None
	  }
	}
/*	
	def processPrivateChatMessage(payload: JsObject): Option[PrivateChatMessage] = {
	     extractPayload(payload) match {
	     case Some(payload) => {
	       payload.asJsObject.fields.get("chat") match {
	         case Some(chat) => {
	           val msg = chat.convertTo[PrivateChatMessage]
	         }
	         case None => println("No chat message")
	       }
	     }
	     case None => println("Message has no payload")
	   }  
	} 
	
*/ 
}
