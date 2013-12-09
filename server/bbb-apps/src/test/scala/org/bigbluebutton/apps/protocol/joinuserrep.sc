package org.bigbluebutton.apps.protocol

import spray.json._
import spray.json.DefaultJsonProtocol
import org.bigbluebutton.apps.models.UsersApp.JoinedUser
import org.bigbluebutton.apps.models.UsersApp.User
import org.bigbluebutton.apps.models.Role
import org.bigbluebutton.apps.models.UsersApp.{WebIdentity, CallerId, VoiceIdentity}

object joinuserrep {

  import UserMessagesProtocol._
  import HeaderAndPayloadJsonSupport._
  import DefaultJsonProtocol._

  val replyHeader = ReplyHeader("replyChannel",  "abc123")
                                                  //> replyHeader  : org.bigbluebutton.apps.protocol.ReplyHeader = ReplyHeader(rep
                                                  //| lyChannel,abc123)
  val headerMeeting = HeaderMeeting("English 101", "english_101", Some("english_101-12345"))
                                                  //> headerMeeting  : org.bigbluebutton.apps.protocol.HeaderMeeting = HeaderMeeti
                                                  //| ng(English 101,english_101,Some(english_101-12345))
  val headerEvent =  HeaderEvent("CreateMeetingRequest", 123456, "web-api", Some(replyHeader))
                                                  //> headerEvent  : org.bigbluebutton.apps.protocol.HeaderEvent = HeaderEvent(Cre
                                                  //| ateMeetingRequest,123456,web-api,Some(ReplyHeader(replyChannel,abc123)))
  val header = Header(headerEvent,  headerMeeting)//> header  : org.bigbluebutton.apps.protocol.Header = Header(HeaderEvent(Create
                                                  //| MeetingRequest,123456,web-api,Some(ReplyHeader(replyChannel,abc123))),Header
                                                  //| Meeting(English 101,english_101,Some(english_101-12345)))
  header.toJson                                   //> res0: spray.json.JsValue = {"event":{"name":"CreateMeetingRequest","timestam
                                                  //| p":123456,"source":"web-api","reply":{"to":"replyChannel","correlationId":"a
                                                  //| bc123"}},"meeting":{"name":"English 101","externalId":"english_101","session
                                                  //| Id":"english_101-12345"}}
  
  
            
  val user = User("user1", "Guga", Role.MODERATOR, 85115, "Welcome to English 101",
                  "http://www.example.com", "http://www.example.com/avatar.png")
                                                  //> user  : org.bigbluebutton.apps.models.UsersApp.User = User(user1,Guga,MODERA
                                                  //| TOR,85115,Welcome to English 101,http://www.example.com,http://www.example.c
                                                  //| om/avatar.png)
  
  val webId = WebIdentity("RichWeb")              //> webId  : org.bigbluebutton.apps.models.UsersApp.WebIdentity = WebIdentity(Ri
                                                  //| chWeb)
  val callerId = CallerId("Richard", "6135207610")//> callerId  : org.bigbluebutton.apps.models.UsersApp.CallerId = CallerId(Rich
                                                  //| ard,6135207610)
  val voiceId = VoiceIdentity("Richard", callerId)//> voiceId  : org.bigbluebutton.apps.models.UsersApp.VoiceIdentity = VoiceIden
                                                  //| tity(Richard,CallerId(Richard,6135207610))
                  
  val juser = JoinedUser("user1", "usertoken", user, false, None, None)
                                                  //> juser  : org.bigbluebutton.apps.models.UsersApp.JoinedUser = JoinedUser(use
                                                  //| r1,usertoken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http://
                                                  //| www.example.com,http://www.example.com/avatar.png),false,None,None)
  juser.toJson                                    //> res1: spray.json.JsValue = {"id":"user1","token":"usertoken","user":{"exter
                                                  //| nalId":"user1","name":"Guga","role":"MODERATOR","pin":85115,"welcomeMessage
                                                  //| ":"Welcome to English 101","logoutUrl":"http://www.example.com","avatarUrl"
                                                  //| :"http://www.example.com/avatar.png"},"isPresenter":false}
  val jurPayload = JoinUserReplyPayload(false, "Successfully joined user.", None)
                                                  //> jurPayload  : org.bigbluebutton.apps.protocol.JoinUserReplyPayload = JoinUs
                                                  //| erReplyPayload(false,Successfully joined user.,None)
  
  val jur = JoinUserReply(header, jurPayload)     //> jur  : org.bigbluebutton.apps.protocol.JoinUserReply = JoinUserReply(Header
                                                  //| (HeaderEvent(CreateMeetingRequest,123456,web-api,Some(ReplyHeader(replyChan
                                                  //| nel,abc123))),HeaderMeeting(English 101,english_101,Some(english_101-12345)
                                                  //| )),JoinUserReplyPayload(false,Successfully joined user.,None))
  jur.toJson                                      //> res2: spray.json.JsValue = {"header":{"event":{"name":"CreateMeetingRequest
                                                  //| ","timestamp":123456,"source":"web-api","reply":{"to":"replyChannel","corre
                                                  //| lationId":"abc123"}},"meeting":{"name":"English 101","externalId":"english_
                                                  //| 101","sessionId":"english_101-12345"}},"payload":{"success":false,"msg":"Su
                                                  //| ccessfully joined user."}}
                                                  
  val juser1 = JoinedUser("user1", "usertoken", user, false, Some(webId), Some(voiceId))
                                                  //> juser1  : org.bigbluebutton.apps.models.UsersApp.JoinedUser = JoinedUser(us
                                                  //| er1,usertoken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http:/
                                                  //| /www.example.com,http://www.example.com/avatar.png),false,Some(WebIdentity(
                                                  //| RichWeb)),Some(VoiceIdentity(Richard,CallerId(Richard,6135207610))))
  
  juser1.toJson                                   //> res3: spray.json.JsValue = {"id":"user1","token":"usertoken","user":{"exter
                                                  //| nalId":"user1","name":"Guga","role":"MODERATOR","pin":85115,"welcomeMessage
                                                  //| ":"Welcome to English 101","logoutUrl":"http://www.example.com","avatarUrl"
                                                  //| :"http://www.example.com/avatar.png"},"isPresenter":false,"webIdent":{"name
                                                  //| ":"RichWeb"},"voiceIdent":{"name":"Richard","callerId":{"name":"Richard","n
                                                  //| umber":"6135207610"}}}
  
  val jur1Payload = JoinUserReplyPayload( true, "Successfully joined user.", Some(juser1))
                                                  //> jur1Payload  : org.bigbluebutton.apps.protocol.JoinUserReplyPayload = JoinU
                                                  //| serReplyPayload(true,Successfully joined user.,Some(JoinedUser(user1,userto
                                                  //| ken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http://www.examp
                                                  //| le.com,http://www.example.com/avatar.png),false,Some(WebIdentity(RichWeb)),
                                                  //| Some(VoiceIdentity(Richard,CallerId(Richard,6135207610))))))
  val jur1 = JoinUserReply(header, jur1Payload)   //> jur1  : org.bigbluebutton.apps.protocol.JoinUserReply = JoinUserReply(Heade
                                                  //| r(HeaderEvent(CreateMeetingRequest,123456,web-api,Some(ReplyHeader(replyCha
                                                  //| nnel,abc123))),HeaderMeeting(English 101,english_101,Some(english_101-12345
                                                  //| ))),JoinUserReplyPayload(true,Successfully joined user.,Some(JoinedUser(use
                                                  //| r1,usertoken,User(user1,Guga,MODERATOR,85115,Welcome to English 101,http://
                                                  //| www.example.com,http://www.example.com/avatar.png),false,Some(WebIdentity(R
                                                  //| ichWeb)),Some(VoiceIdentity(Richard,CallerId(Richard,6135207610)))))))
  jur1.toJson                                     //> res4: spray.json.JsValue = {"header":{"event":{"name":"CreateMeetingRequest
                                                  //| ","timestamp":123456,"source":"web-api","reply":{"to":"replyChannel","corre
                                                  //| lationId":"abc123"}},"meeting":{"name":"English 101","externalId":"english_
                                                  //| 101","sessionId":"english_101-12345"}},"payload":{"success":true,"msg":"Suc
                                                  //| cessfully joined user.","user":{"id":"user1","token":"usertoken","user":{"e
                                                  //| xternalId":"user1","name":"Guga","role":"MODERATOR","pin":85115,"welcomeMes
                                                  //| sage":"Welcome to English 101","logoutUrl":"http://www.example.com","avatar
                                                  //| Url":"http://www.example.com/avatar.png"},"isPresenter":false,"webIdent":{"
                                                  //| name":"RichWeb"},"voiceIdent":{"name":"Richard","callerId":{"name":"Richard
                                                  //| ","number":"6135207610"}}}}}
  
 // val payload =

}