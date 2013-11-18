package org.bigbluebutton.apps

import org.apache.commons.codec.digest.DigestUtils

/**
 * Some utilities.
 */
object Util {

  /**
   * Convert the external meeting id passed from 3rd-party applications
   * into an internal meeting id.
   */
  def toInternalMeetingId(externalMeetingId: String) = 
    DigestUtils.sha1Hex(externalMeetingId)
}