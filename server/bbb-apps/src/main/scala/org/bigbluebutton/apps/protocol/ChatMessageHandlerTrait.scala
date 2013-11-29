package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.models.UserIdAndName

case class ChatTranslation(lang: String, text: String)
case class TextFont(color: Int, size: Int, fontType: String)
case class ChatMessage(timestamp: Long, chatType: String, from: UserIdAndName, 
                       to: Option[UserIdAndName], lang: String,
                       font: TextFont, text: String, 
                       translations: Option[Seq[ChatTranslation]])
case class ReceivedMessage(id: String, message: ChatMessage, 
                       translations: Seq[ChatTranslation])
                       
trait ChatMessageHandlerTrait {

}