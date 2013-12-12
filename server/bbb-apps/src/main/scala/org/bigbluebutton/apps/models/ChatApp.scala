package org.bigbluebutton.apps.models

import org.bigbluebutton.apps.models.ChatApp._
import org.bigbluebutton.apps.users.UsersApp._

object ChatApp {
	case class Chatter(id: String, name: String)
	case class ChatFont(color: String, size: Int, fontType: String)
	case class ChatText(lang: String, text: String)
	
	case class PublicChatMessage(id: String, from: Chatter, message: ChatText, translations: Seq[ChatText])
	case class PrivateChatMessage(id: String, from: Chatter, message: ChatText, translations: Seq[ChatText], to: Chatter)  
	
	case class ChatTranslation(lang: String, text: String)
    case class TextFont(color: Int, size: Int, fontType: String)
    case class ChatMessage(timestamp: Long, chatType: String, from: UserIdAndName, 
                       to: Option[UserIdAndName], lang: String,
                       font: TextFont, text: String, 
                       translations: Option[Seq[ChatTranslation]])
    case class ReceivedMessage(id: String, message: ChatMessage, 
                       translations: Seq[ChatTranslation])

    case class ChatConversation(id: String, messages: Seq[ChatMessage])
}

class ChatApp {
  private val chats = new collection.immutable.HashMap[String, ChatConversation]()
}
