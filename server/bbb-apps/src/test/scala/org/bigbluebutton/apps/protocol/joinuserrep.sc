package org.bigbluebutton.apps.protocol

import spray.json._
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.UsersApp.JoinedUser
import org.bigbluebutton.apps.models.UsersApp.User
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.models.UsersApp.{WebIdentity, CallerId, VoiceIdentity}

object joinuserrep {

  import RegisterUserRequestProtocol._
  import HeaderAndPayloadJsonSupport._
  
  object MyHeaderAndPayloadJsonSupport extends DefaultJsonProtocol {
	  implicit val webIdentityFormat = jsonFormat1(WebIdentity)
	  implicit val callerIdFormat = jsonFormat2(CallerId)
	  implicit val voiceIdentityFormat = jsonFormat2(VoiceIdentity)
	  
	  implicit val userFormat = jsonFormat7(User)
	  implicit val joinedUserFormat = jsonFormat6(JoinedUser)
	  
	  implicit val joinUserReplyFormat = jsonFormat4(JoinUserReply)
  }

  import MyHeaderAndPayloadJsonSupport._

  val replyHeader = ReplyHeader("replyChannel",  "abc123")
                                                  //> replyHeader  : org.bigbluebutton.apps.protocol.ReplyHeader = ReplyHeader(rep
                                                  //| lyChannel,abc123)
  val headerMeeting = HeaderMeeting("English 101", "english_101", Some("english_101-12345"))
                                                  //> headerMeeting  : org.bigbluebutton.apps.protocol.HeaderMeeting = HeaderMeet
                                                  //| ing(English 101,english_101,Some(english_101-12345))
  val headerEvent =  HeaderEvent("CreateMeetingRequest", 123456, "web-api", Some(replyHeader))
                                                  //> headerEvent  : org.bigbluebutton.apps.protocol.HeaderEvent = HeaderEvent(Cr
                                                  //| eateMeetingRequest,123456,web-api,Some(ReplyHeader(replyChannel,abc123)))
  val header = Header(headerEvent,  headerMeeting)//> header  : org.bigbluebutton.apps.protocol.Header = Header(HeaderEvent(Creat
                                                  //| eMeetingRequest,123456,web-api,Some(ReplyHeader(replyChannel,abc123))),Head
                                                  //| erMeeting(English 101,english_101,Some(english_101-12345)))
  header.toJson                                   //> res0: spray.json.JsValue = {"event":{"name":"CreateMeetingRequest","timesta
                                                  //| mp":123456,"source":"web-api","reply":{"to":"replyChannel","correlationId":
                                                  //| "abc123"}},"meeting":{"name":"English 101","externalId":"english_101","sess
                                                  //| ionId":"english_101-12345"}}
  
  val huser = JoinUserReply(header, true, "Successfully joined user.", None)
                                                  //> huser  : org.bigbluebutton.apps.protocol.JoinUserReply = JoinUserReply(Head
                                                  //| er(HeaderEvent(CreateMeetingRequest,123456,web-api,Some(ReplyHeader(replyCh
                                                  //| annel,abc123))),HeaderMeeting(English 101,english_101,Some(english_101-1234
                                                  //| 5))),true,Successfully joined user.,None)
  huser.toJson                                    //> res1: spray.json.JsValue = {"header":{"event":{"name":"CreateMeetingRequest
                                                  //| ","timestamp":123456,"source":"web-api","reply":{"to":"replyChannel","corre
                                                  //| lationId":"abc123"}},"meeting":{"name":"English 101","externalId":"english_
                                                  //| 101","sessionId":"english_101-12345"}},"success":true,"msg":"Successfully j
                                                  //| oined user."}
            
  val user = User("user1", "Guga", Role.MODERATOR, 85115, "Welcome to English 101",
                  "http://www.example.com", "http://www.example.com/avatar.png")
                                                  //> user  : org.bigbluebutton.apps.models.UsersApp.User = User(user1,Guga,MODER
                                                  //| ATOR,85115,Welcome to English 101,http://www.example.com,http://www.example
                                                  //| .com/avatar.png)
  
  val webId = WebIdentity("RichWeb")              //> webId  : org.bigbluebutton.apps.models.UsersApp.WebIdentity = WebIdentity(R
                                                  //| ichWeb)
  val callerId = CallerId("Richard Alam", "6135207610")
                                                  //> callerId  : org.bigbluebutton.apps.models.UsersApp.CallerId = CallerId(Rich
                                                  //| ard Alam,6135207610)
  val voiceId = VoiceIdentity("Richard", callerId)//> voiceId  : org.bigbluebutton.apps.models.UsersApp.VoiceIdentity = VoiceIden
                                                  //| tity(Richard,CallerId(Richard Alam,6135207610))
                  
  val juser = JoinedUser("user1", "usertoken", user, false, None, None)
                                                  //> juser  : org.bigbluebutton.apps.models.UsersApp.JoinedUser = JoinedUser(use
                                                  //| r1,usertoken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http://
                                                  //| www.example.com,http://www.example.com/avatar.png),false,None,None)
  juser.toJson                                    //> res2: spray.json.JsValue = {"id":"user1","token":"usertoken","user":{"exter
                                                  //| nalId":"user1","name":"Guga","role":"MODERATOR","pin":85115,"welcomeMessage
                                                  //| ":"Welcome to English 101","logoutUrl":"http://www.example.com","avatarUrl"
                                                  //| :"http://www.example.com/avatar.png"},"isPresenter":false}
                                                  
  val juser1 = JoinedUser("user1", "usertoken", user, false, Some(webId), Some(voiceId))
                                                  //> juser1  : org.bigbluebutton.apps.models.UsersApp.JoinedUser = JoinedUser(us
                                                  //| er1,usertoken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http:/
                                                  //| /www.example.com,http://www.example.com/avatar.png),false,Some(WebIdentity(
                                                  //| RichWeb)),Some(VoiceIdentity(Richard,CallerId(Richard Alam,6135207610))))
  
  juser1.toJson                                   //> res3: spray.json.JsValue = {"id":"user1","token":"usertoken","user":{"exter
                                                  //| nalId":"user1","name":"Guga","role":"MODERATOR","pin":85115,"welcomeMessage
                                                  //| ":"Welcome to English 101","logoutUrl":"http://www.example.com","avatarUrl"
                                                  //| :"http://www.example.com/avatar.png"},"isPresenter":false,"webIdent":{"name
                                                  //| ":"RichWeb"},"voiceIdent":{"name":"Richard","callerId":{"name":"Richard Ala
                                                  //| m","number":"6135207610"}}}
}