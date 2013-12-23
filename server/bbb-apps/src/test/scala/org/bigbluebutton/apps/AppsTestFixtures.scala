package org.bigbluebutton.apps

import org.bigbluebutton.apps.models._
import org.bigbluebutton.apps.users.data._

trait AppsTestFixtures {
  
  val maxUsers =  20
  val duration = MeetingDuration(120, true, 160)
  val voiceConference = VoiceConference(85115, 1234)
  val phone1 =  PhoneNumber("613-555-7600", "Ottawa")
  val phone2 = PhoneNumber("1-888-555-7890", "NA Toll-Free")
  val metadata = Map("customerId" -> "acme-customer",
	                "customerName" -> "ACME")

  val meetingIdAndName = MeetingIdAndName("english_101", "English 101")
  val eng101Session = Session("english_101-1234", meetingIdAndName)  
  
  val eng101Desc = MeetingDescriptor("english_101", "English 101",  
                       true, "Welcome to English 101", 
                       "http://www.bigbluebutton.org", 
                       "http://www.gravatar.com/bigbluebutton",
                       maxUsers, duration, 
                       voiceConference, Seq(phone1, phone2), 
                       metadata)

  val userJuan = User("userjuan", "Juan Tamad", 
	                Role.MODERATOR, 12345, "Welcome Juan",
	                "http://www.umaliska.don", "http://www.mukhamo.com/unggoy")                        
  val juanWebIdentity = WebIdentity(false)
  val juanCallerId = CallerId("Juan Tamad", "011-63-917-555-1234") 
  val juanVoiceMeta = Map("userid" -> "1", "conference_num" -> "85115")
  val juanVoiceIdentity = VoiceIdentity(juanCallerId, false, 
                         false, false, juanVoiceMeta)
                         
  val joinedUserJuan = JoinedUser("juanid", "juanToken", userJuan,
	                      true, juanWebIdentity, juanVoiceIdentity) 

  val userAsyong = User("userasyong", "Asyong Aksaya", 
	                Role.VIEWER, 12346, "Welcome Asyong",
	                "http://www.bilmoko.nyan", "http://www.mukhamo.com/pera") 	                      
  val asyongWebIdentity = WebIdentity(true)
  val asyongCallerId = CallerId("Asyong Aksaya", "011-63-917-555-1234") 
  val asyongVoiceMeta = Map("userid" -> "2", "conference_num" -> "85115")
  val asyongVoiceIdentity = VoiceIdentity(asyongCallerId, false, 
                         false, false, asyongVoiceMeta)
                         
  val joinedUserAsyong = JoinedUser("asyongid", "asyongToken", userAsyong,
	                      true, asyongWebIdentity, asyongVoiceIdentity)
}