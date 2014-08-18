package org.bigbluebutton.core

import scala.actors.Actor
import scala.actors.Actor._
import org.bigbluebutton.core.apps.poll.PollApp
import org.bigbluebutton.core.apps.poll.Poll
import org.bigbluebutton.core.apps.poll.PollApp
import org.bigbluebutton.core.apps.users.UsersApp
import org.bigbluebutton.core.api._
import org.bigbluebutton.core.apps.presentation.PresentationApp
import org.bigbluebutton.core.apps.layout.LayoutApp
import org.bigbluebutton.core.apps.chat.ChatApp
import org.bigbluebutton.core.apps.whiteboard.WhiteboardApp
import scala.actors.TIMEOUT
import java.util.concurrent.TimeUnit

case object StopMeetingActor
                      
class MeetingActor(val meetingID: String, meetingName: String, val recorded: Boolean, 
                   val voiceBridge: String, duration: Long, val outGW: MessageOutGateway) 
                   extends Actor with UsersApp with PresentationApp
                   with PollApp with LayoutApp with ChatApp
                   with WhiteboardApp {  

  var permissionsInited = false
  var permissions = new Permissions()
  var recording = false;
  var muted = false;
  var meetingEnded = false
  
  val TIMER_INTERVAL = 30000
  val EXTENSION_TIME = 30
  val startedOn = timeNowInMinutes
  var hasUserJoined = false
  var hasSentExtendNotice = false
  var hasReceivedExtendNoticeReply = false
  var minutesToExtend = 0
  
  var lastUserLeftOn:Long = 0
  var hasLastWebUserLeft = false
  var lastWebUserLeftOn:Long = 0
  
  class TimerActor(val timeout: Long, val who: Actor, val reply: String) extends Actor {
    def act {
        reactWithin(timeout) {
          case TIMEOUT => who ! reply; exit
        }
    }
  }
  
  def act() = {
	loop {
	  react {
	    case "StartTimer"                                => handleStartTimer
	    case "CheckEndMeeting"                           => handleCheckEndMeeting
	    case "MonitorNumberOfWebUsers"                   => handleMonitorNumberOfWebUsers()
	    case msg: ValidateAuthToken                      => handleValidateAuthToken(msg)
	    case msg: RegisterUser                           => handleRegisterUser(msg)
	    case msg: VoiceUserJoined                        => handleVoiceUserJoined(msg)
	    case msg: VoiceUserLeft                          => handleVoiceUserLeft(msg)
	    case msg: VoiceUserMuted                         => handleVoiceUserMuted(msg)
	    case msg: VoiceUserTalking                       => handleVoiceUserTalking(msg)
    	case msg: UserJoining                            => handleUserJoin(msg)
	    case msg: UserLeaving                            => handleUserLeft(msg)
	    case msg: AssignPresenter                        => handleAssignPresenter(msg)
	    case msg: GetUsers                               => handleGetUsers(msg)
	    case msg: ChangeUserStatus                       => handleChangeUserStatus(msg)
	    case msg: UserRaiseHand                          => handleUserRaiseHand(msg)
	    case msg: UserLowerHand                          => handleUserLowerHand(msg)
	    case msg: UserShareWebcam                        => handleUserShareWebcam(msg)
	    case msg: UserUnshareWebcam                      => handleUserunshareWebcam(msg)
	    case msg: MuteMeetingRequest                     => handleMuteMeetingRequest(msg)
	    case msg: MuteAllExceptPresenterRequest          => handleMuteAllExceptPresenterRequest(msg)
	    case msg: IsMeetingMutedRequest                  => handleIsMeetingMutedRequest(msg)
	    case msg: MuteUserRequest                        => handleMuteUserRequest(msg)
	    case msg: EjectUserRequest                       => handleEjectUserRequest(msg)
	    case msg: SetLockSettings                        => handleSetLockSettings(msg)
	    case msg: InitLockSettings                       => handleInitLockSettings(msg)
	    case msg: GetChatHistoryRequest                  => handleGetChatHistoryRequest(msg) 
	    case msg: SendPublicMessageRequest               => handleSendPublicMessageRequest(msg)
	    case msg: SendPrivateMessageRequest              => handleSendPrivateMessageRequest(msg)
	    case msg: UserConnectedToGlobalAudio             => handleUserConnectedToGlobalAudio(msg)
	    case msg: UserDisconnectedFromGlobalAudio        => handleUserDisconnectedFromGlobalAudio(msg)
	    case msg: GetCurrentLayoutRequest                => handleGetCurrentLayoutRequest(msg)
	    case msg: BroadcastLayoutRequest                 => handleBroadcastLayoutRequest(msg)
	    case msg: InitializeMeeting                      => handleInitializeMeeting(msg)
    	case msg: ClearPresentation                      => handleClearPresentation(msg)
    	case msg: PresentationConversionUpdate           => handlePresentationConversionUpdate(msg)
    	case msg: PresentationPageCountError             => handlePresentationPageCountError(msg)
    	case msg: PresentationSlideGenerated             => handlePresentationSlideGenerated(msg)
    	case msg: PresentationConversionCompleted        => handlePresentationConversionCompleted(msg)
    	case msg: RemovePresentation                     => handleRemovePresentation(msg)
    	case msg: GetPresentationInfo                    => handleGetPresentationInfo(msg)
    	case msg: SendCursorUpdate                       => handleSendCursorUpdate(msg)
    	case msg: ResizeAndMoveSlide                     => handleResizeAndMoveSlide(msg)
    	case msg: GotoSlide                              => handleGotoSlide(msg)
    	case msg: SharePresentation                      => handleSharePresentation(msg)
    	case msg: GetSlideInfo                           => handleGetSlideInfo(msg)
    	case msg: PreuploadedPresentations               => handlePreuploadedPresentations(msg)
      case msg: PreCreatedPoll                         => handlePreCreatedPoll(msg)
      case msg: CreatePoll                             => handleCreatePoll(msg)
      case msg: UpdatePoll                             => handleUpdatePoll(msg)
      case msg: DestroyPoll                            => handleDestroyPoll(msg)
      case msg: RemovePoll                             => handleRemovePoll(msg)
      case msg: SharePoll                              => handleSharePoll(msg)
      case msg: StopPoll                               => handleStopPoll(msg)
      case msg: StartPoll                              => handleStartPoll(msg)
      case msg: ClearPoll                              => handleClearPoll(msg)
      case msg: GetPolls                               => handleGetPolls(msg)
      case msg: RespondToPoll                          => handleRespondToPoll(msg)
      case msg: HidePollResult                         => handleHidePollResult(msg)
      case msg: ShowPollResult                         => handleShowPollResult(msg)
	    case msg: SendWhiteboardAnnotationRequest        => handleSendWhiteboardAnnotationRequest(msg)
	    case msg: GetWhiteboardShapesRequest             => handleGetWhiteboardShapesRequest(msg)
	    case msg: ClearWhiteboardRequest                 => handleClearWhiteboardRequest(msg)
	    case msg: UndoWhiteboardRequest                  => handleUndoWhiteboardRequest(msg)
	    case msg: EnableWhiteboardRequest                => handleEnableWhiteboardRequest(msg)
	    case msg: IsWhiteboardEnabledRequest             => handleIsWhiteboardEnabledRequest(msg)
	    case msg: SetRecordingStatus                     => handleSetRecordingStatus(msg)
	    case msg: GetRecordingStatus                     => handleGetRecordingStatus(msg)
	    case msg: VoiceRecording                         => handleVoiceRecording(msg)
	    case msg: ExtendMeetingNoticeReply               => handleExtendMeetingNoticeReply(msg)
	    case msg: EndMeeting                             => handleEndMeeting(msg)
	    case StopMeetingActor                            => exit
	    case _ => // do nothing
	  }
	}
  }	
  
  def hasMeetingEnded():Boolean = {
    meetingEnded
  }
  
  private def handleStartTimer() {
    println("*************** timer started******************")
    schedTimer()
  }
  
  private def schedTimer() {
    val timerActor = new TimerActor(TIMER_INTERVAL, self, "Hello")
    timerActor.start    
  }
  
  def timeNowInMinutes():Long = {
    TimeUnit.NANOSECONDS.toMinutes(System.nanoTime())
  }
  
  private def noUserJoined():Boolean = {
    val now = timeNowInMinutes
    if (!hasUserJoined && (now - startedOn > 2)) {
      println("No user has joined in 2 minutes")
      true
    } else {
      false
    }
  }
  
  private def isMeetingPassedDuration():Boolean = {
    val now = timeNowInMinutes
    println("now=[" + now + "], startedOn=[" + startedOn + "], duration=[" + duration + "]")
    if ((duration > 0) && (now > startedOn + duration)) {
      true
    } else {
      false
    }
  }
  
  private def meetingEmptyFor5Minutes():Boolean = {
    val now = timeNowInMinutes
    if (lastUserLeftOn > 0) {
      now - lastUserLeftOn > 5
    } else {
      false
    }
  }
  private def lessThanMinToEnd(numMinutes: Long):Boolean = {
     (timeNowInMinutes - (startedOn + duration + minutesToExtend)) < numMinutes
  }
  
  private def seeIfMeetingNeedsToBeExtended() {
    val now = timeNowInMinutes
    if ((duration > 0) && lessThanMinToEnd(2) && !hasSentExtendNotice) {
      sendMeetingEndNotice(2, EXTENSION_TIME)
      hasSentExtendNotice = true
    } else if ((duration > 0) && lessThanMinToEnd(5)) {
      sendMeetingEndNotice(5, EXTENSION_TIME)
    } else if ((duration > 0) && lessThanMinToEnd(15)) {
      sendMeetingEndNotice(15, EXTENSION_TIME)
    }
  }
  
  private def handleCheckEndMeeting() {
    println("*************** timer fired on [" + timeNowInMinutes + "]******************")
    if (noUserJoined || meetingEmptyFor5Minutes) {
      println("Ending meeting as no user joined in 2 minutes")
      endMeeting()
    } else {
      if (isMeetingPassedDuration()) {
        endMeeting()
      } else {
        seeIfMeetingNeedsToBeExtended()
      	schedTimer()        
      } 
    }
  }
  
  private def sendMeetingEndNotice(minutesLeft: Int, minutesToExtend: Int) {
    val moderators = users.getModerators
    println("Sending meeting end notice. minLeft=[" + minutesLeft + "], minExtend=[" + minutesToExtend + "]")
    outGW.send(new ExtendMeetingNotice(meetingID, recorded, minutesLeft, minutesToExtend, moderators))
  }
  
  private def handleExtendMeetingNoticeReply(msg: ExtendMeetingNoticeReply) {
    if (msg.extend) {
      minutesToExtend += EXTENSION_TIME
      hasSentExtendNotice = false
      users.getUser(msg.extendedBy) foreach { u=>
        outGW.send(new MeetingExtended(meetingID, recorded, u))
      }
    }
  }
   
  def webUserJoined() {
    if (users.numWebUsers > 0) {
      lastWebUserLeftOn = 0
	  }      
  }
  
  def startCheckingIfWeNeedToEndVoiceConf() {
    if (users.numWebUsers == 0) {
      lastWebUserLeftOn = timeNowInMinutes
	    println("*************** MonitorNumberOfWebUsers started ******************")
      scheduleEndVoiceConference()
	  }
  }
  
  def handleMonitorNumberOfWebUsers() {
    if (users.numWebUsers == 0 && lastWebUserLeftOn > 0) {
      if (timeNowInMinutes - lastWebUserLeftOn > 2) {
        println("*************** MonitorNumberOfWebUsers [Ject all from voice] ******************")
        outGW.send(new EjectAllVoiceUsers(meetingID, recorded, voiceBridge))
      } else {
        scheduleEndVoiceConference()
      }
    }
  }
  
  private def scheduleEndVoiceConference() {
    println("*************** MonitorNumberOfWebUsers monitor ******************")
    val timerActor = new TimerActor(TIMER_INTERVAL, self, "MonitorNumberOfWebUsers")
    timerActor.start    
  }
  
  def sendMeetingHasEnded(userId: String) {
    outGW.send(new MeetingHasEnded(meetingID, userId))
    outGW.send(new DisconnectUser(meetingID, userId))
  }
  
  private def handleEndMeeting(msg: EndMeeting) {
    endMeeting()
  }
  
  private def endMeeting() {
    meetingEnded = true
    outGW.send(new MeetingEnded(meetingID, recorded, voiceBridge))
    outGW.send(new DisconnectAllUsers(meetingID))    
  }
  
  private def handleVoiceRecording(msg: VoiceRecording) {
     if (msg.recording) {
       outGW.send(new VoiceRecordingStarted(meetingID, 
                        recorded, msg.recordingFile, 
                        msg.timestamp, voiceBridge))
     } else {
       outGW.send(new VoiceRecordingStopped(meetingID, recorded, 
                        msg.recordingFile, msg.timestamp, voiceBridge))
     }
  }
  
  private def handleSetRecordingStatus(msg: SetRecordingStatus) {
     recording = msg.recording
     outGW.send(new RecordingStatusChanged(meetingID, recorded, msg.userId, msg.recording))
  }   

  private def handleGetRecordingStatus(msg: GetRecordingStatus) {
     outGW.send(new GetRecordingStatusReply(meetingID, recorded, msg.userId, recording.booleanValue()))
  }
  
  def lockLayout(lock: Boolean) {
    permissions = permissions.copy(lockedLayout=lock)
  }
  
  def newPermissions(np: Permissions) {
    permissions = np
  }
  
  def permissionsEqual(other: Permissions):Boolean = {
    permissions == other
  }
  
}
