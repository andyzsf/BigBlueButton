package org.bigbluebutton.apps.protocol

import org.bigbluebutton.apps.models._


trait MeetingTestFixtures {
  
  val durationCOnfig = MeetingDuration(120, false, 30)
  val voiceConfig = VoiceConference(12345, 85115)
  val phone1 = PhoneNumber("613-520-7600", "Ottawa") 
  val phone2 = PhoneNumber("1-888-555-7890", "NA Toll-Free")
  val metadata = Map("customerId" -> "acme-customer",
                     "customerName" -> "ACME")


}