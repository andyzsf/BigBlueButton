package org.bigbluebutton.endpoint.redis

object InMsgNameConst {
  val CreateMeetingRequest     = "create_meeting_request"
  val CreateMeetingResponse    = "create_meeting_response"
  val MeetingCreatedEvent      = "meeting_created_event"
  val EndMeetingRequest        = "end_meeting_request"
  val EndMeetingResponse       = "end_meeting_response"
  val MeetingEndEvent          = "meeting_end_event"
  val MeetingEndedEvent        = "meeting_ended_event"
    
  val RegisterUserRequest      = "register_user_request"
  val RegisterUserResponse     = "register_user_response"
  val UserRegisteredEvent      = "user_registered_event"
  val UserJoinRequest          = "user_join_request"
  val UserJoinResponse         = "user_join_response"
  val UserJoinedEvent          = "user_joined_event"
  val UserLeaveEvent           = "user_leave_request"
  val UserLeftEvent            = "user_left_event"
  val GetUsersRequest          = "get_users_request"
  val GetUsersResponse         = "get_users_response"
  val RaiseUserHandRequest     = "raise_user_hand_request"
  val UserRaisedHandEvent      = "user_raised_hand_event"
  val AssignPresenterRequest   = "assign_presenter_request"
  val PresenterAssignedEvent   = "presenter_assigned_event"
  val MuteUserRequest          = "mute_user_request"
  val MuteUserRequestEvent     = "mute_user_request_event"
  val UserMutedEvent           = "user_muted_event"  
    
  val MuteUserVoiceRequest   = "mute_user_voice_conf_request"
}

