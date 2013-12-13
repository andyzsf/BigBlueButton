package org.bigbluebutton.apps

import org.bigbluebutton.apps.MeetingMessage.CreateMeeting
import org.bigbluebutton.apps.models._
import org.bigbluebutton.apps.users.Model._

trait MeetingManagerTestFixtures {

  def generateMeetingSession():Session = {
    Session("english_101-1234", "english_101", "English 101")  
  }
  
  def generateUserJuan():User = {
    	User("userjuan", "Juan Tamad", 
	                Role.MODERATOR, 12345, "Welcome Juan",
	                "http://www.umaliska.don", "http://www.mukhamo.com/unggoy") 
  }

  def generateUserAsyong():User = {
    	User("userasyong", "Asyong Aksaya", 
	                Role.VIEWER, 12346, "Welcome Asyong",
	                "http://www.bilmoko.nyan", "http://www.mukhamo.com/pera") 
  }
    
  def generateMeetingDescriptor():MeetingDescriptor = {
    val maxUsers =  MaxUsers(20, true)
    val duration = MeetingDuration(120, true, 160)
    val pin = VoiceConfAndPin(85115, 1234)
    val phone1 =  PhoneNumber("613-555-7600", "Ottawa")
    val phone2 = PhoneNumber("1-888-555-7890", "NA Toll-Free")
    val metadata = Map("customerId" -> "acme-customer",
	                "customerName" -> "ACME")
	                
    MeetingDescriptor("english_101", "English 101",  
                       true, "Welcome to English 101", 
                       "http://www.bigbluebutton.org", "http://www.gravatar.com/bigbluebutton",
                       maxUsers, duration, 
                       pin, Seq(phone1, phone2), 
                       metadata)    
  }
  
  def generateCreateMeetingMessage():CreateMeeting = {  
     CreateMeeting(generateMeetingDescriptor())
  }
}