package org.bigbluebutton.endpoint.redis

object InMsgNameConst {
  val CreateMeetingRequest     = "create_meeting_request"
  val CreateMeetingResponse    = "create_meeting_response"
  val MeetingCreatedEvent      = "meeting_created_event"
  val RegisterUserRequest      = "register_user_request"
  val RegisterUserResponse     = "register_user_response"
  val UserRegisteredEvent      = "user_registered_event"
  val UserJoinRequest          = "user_join_request"
  val UserJoinResponse         = "user_join_response"
  val UserJoinedEvent          = "user_joined_event"
  val UserLeaveEvent           = "user_leave_request"
  val UserLeftEvent            = "user_left_event"
  val GetUsersRequest          = "get_users_request"
  val AssignPresenterRequest   = "assign_presenter_request"
  val PresenterAssignedEvent   = "presenter_assigned_event"
}

