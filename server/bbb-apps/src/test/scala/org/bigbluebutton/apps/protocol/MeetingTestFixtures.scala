package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.models.MaxUsers
import org.bigbluebutton.apps.models.MeetingDuration
import org.bigbluebutton.apps.models.PhoneNumber
import org.bigbluebutton.apps.models.VoiceConfAndPin

trait MeetingTestFixtures {
  
  val usersConfig = MaxUsers(20, true)
  val durationCOnfig = MeetingDuration(120, false, 30)
  val voiceConfig = VoiceConfAndPin(12345, 85115)
  val phone1 = PhoneNumber("613-520-7600", "Ottawa") 
  val phone2 = PhoneNumber("1-888-555-7890", "NA Toll-Free")
  val metadata = Map("customerId" -> "acme-customer",
                     "customerName" -> "ACME")

  val meetingConfig = MeetingDescriptor(
                          "English 101", "english_101", true,
                          "Welcome to English 101",
                          "http://www.bigbluebutton.org",
                          "http://www.gravatar.com/bigbluebutton",
                          usersConfig, durationCOnfig,
                          voiceConfig, Seq(phone1, phone2),
                          metadata)
}