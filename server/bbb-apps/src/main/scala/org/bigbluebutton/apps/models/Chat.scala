package org.bigbluebutton.apps.models

object Chat {
	case class Chatter(id: String, name: String)
	case class ChatFont(color: String, size: Int, fontType: String)
	case class ChatText(lang: String, text: String)
	
	case class PublicChatMessage(id: String, from: Chatter, message: ChatText, translations: Seq[ChatText])
	case class PrivateChatMessage(id: String, from: Chatter, message: ChatText, translations: Seq[ChatText], to: Chatter)  
}
