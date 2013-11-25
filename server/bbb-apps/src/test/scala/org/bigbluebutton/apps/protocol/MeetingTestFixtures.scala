package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.models.MeetingConfig
import org.bigbluebutton.apps.models.UsersConfig
import org.bigbluebutton.apps.models.DurationConfig
import org.bigbluebutton.apps.models.PhoneNumberConfig
import org.bigbluebutton.apps.models.VoiceConfig

trait MeetingTestFixtures {
  
  val usersConfig = UsersConfig(20, true)
  val durationCOnfig = DurationConfig(120, false, 30)
  val voiceConfig = VoiceConfig(12345, 85115)
  val phone1 = PhoneNumberConfig("613-520-7600", "Ottawa") 
  val phone2 = PhoneNumberConfig("1-888-555-7890", "NA Toll-Free")
  val metadata = Map("customerId" -> "acme-customer",
                     "customerName" -> "ACME")

  val meetingConfig = MeetingConfig(
                          "English 101", "english_101", true,
                          "Welcome to English 101",
                          "http://www.bigbluebutton.org",
                          "http://www.gravatar.com/bigbluebutton",
                          usersConfig, durationCOnfig,
                          voiceConfig, Seq(phone1, phone2),
                          metadata)
}