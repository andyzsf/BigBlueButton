package org.bigbluebutton.core.apps.users

import org.bigbluebutton.core.api._
import scala.collection.mutable.HashMap
import org.bigbluebutton.core.User
import java.util.ArrayList
import org.bigbluebutton.core.MeetingActor
import scala.collection.mutable.ArrayBuffer
import javax.print.attribute.standard.NumberOfDocuments

trait UsersApp {
  this : MeetingActor =>
  
  val outGW: MessageOutGateway
  
  val users = new UsersModel
  private var regUsers = new collection.immutable.HashMap[String, RegisteredUser]
  
  private var locked = false
  private var meetingMuted = false
  
  private var currentPresenter = new Presenter("system", "system", "system")
  
  def hasUser(userID: String):Boolean = {
    users.hasUser(userID)
  }
  
  def getUser(userID:String):Option[UserVO] = {
    users.getUser(userID)
  }
  
  def getCurrentPresenter:Presenter = {
    currentPresenter
  }
  
  def handleUserConnectedToGlobalAudio(msg: UserConnectedToGlobalAudio) {
//    println("*************** Got UserConnectedToGlobalAudio message for [" + msg.name + "] ********************" )
    val user = users.getUserWithExternalId(msg.userid)
    user foreach {u =>
      val vu = u.voiceUser.copy(talking=false, joined=true)
      val uvo = u.copy(listenOnly=true, voiceUser=vu, phoneUser=false, calledFromBbb=true)
      users.addUser(uvo)
      outGW.send(new UserListeningOnly(meetingID, recorded, uvo.userID, uvo.listenOnly))        
    }
  }
  
  def handleUserDisconnectedFromGlobalAudio(msg: UserDisconnectedFromGlobalAudio) {
    println("*************** Got UserDisconnectedToGlobalAudio message for [" + msg.name + "] ********************" )
    val user = users.getUserWithExternalId(msg.userid)
    user foreach {u =>
      val vu = u.voiceUser.copy(talking=false, joined=false)
      val uvo = u.copy(listenOnly=false, phoneUser=false, calledFromBbb=false)
      users.addUser(uvo)
      outGW.send(new UserListeningOnly(meetingID, recorded, uvo.userID, uvo.listenOnly))        
    }
  }
  
  def handleMuteAllExceptPresenterRequest(msg: MuteAllExceptPresenterRequest) {
    meetingMuted = msg.mute
    outGW.send(new MeetingMuted(meetingID, recorded, meetingMuted))
    
    usersWhoAreNotPresenter foreach {u =>
      outGW.send(new MuteVoiceUser(meetingID, recorded, msg.requesterID, u.userID, voiceBridge, u.voiceUser.userId, msg.mute))
    }       
  }
    
  def handleMuteMeetingRequest(msg: MuteMeetingRequest) {
    meetingMuted = msg.mute
    outGW.send(new MeetingMuted(meetingID, recorded, meetingMuted))
    users.getUsers foreach {u =>
        outGW.send(new MuteVoiceUser(meetingID, recorded, msg.requesterID, u.userID, voiceBridge, u.voiceUser.userId, msg.mute)) 
    }
  }
  
  def handleValidateAuthToken(msg: ValidateAuthToken) {
//    println("*************** Got ValidateAuthToken message ********************" )
    regUsers.get (msg.userId) match {
      case Some(u) => outGW.send(new ValidateAuthTokenReply(meetingID, msg.userId, msg.token, true, msg.correlationId))
      case None => outGW.send(new ValidateAuthTokenReply(meetingID, msg.userId, msg.token, false, msg.correlationId))
    }  
  }
  
  def handleRegisterUser(msg: RegisterUser) {
    if (hasMeetingEnded) {
      // Check first if the meeting has ended and the user refreshed the client to re-connect.
      sendMeetingHasEnded(msg.userID)
    } else {
      logger.info("Handling RegisterUser. mid=[" + meetingID + "]  wid=[" + msg.userID + "] pin=[" + msg.pin + "]")
      val regUser = new RegisteredUser(msg.userID, msg.extUserID, msg.name, msg.role, msg.authToken, msg.pin)
      regUsers += msg.userID -> regUser
      outGW.send(new UserRegistered(meetingID, recorded, regUser))      
    }

  }
  
  def handleIsMeetingMutedRequest(msg: IsMeetingMutedRequest) {
    outGW.send(new IsMeetingMutedReply(meetingID, recorded, msg.requesterID, meetingMuted))
  }
  
  def handleMuteUserRequest(msg: MuteUserRequest) {
//    println("Received mute user request uid=[" + msg.userID + "] mute=[" + msg.mute + "]")
    users.getUser(msg.userID) match {
      case Some(u) => {
//        println("Sending mute user request uid=[" + msg.userID + "] mute=[" + msg.mute + "]")
        outGW.send(new MuteVoiceUser(meetingID, recorded, msg.requesterID, u.userID, voiceBridge, u.voiceUser.userId, msg.mute))
      }
      case None => {
//        println("Could not find user to mute. uid=[" + msg.userID + "] mute=[" + msg.mute + "]")
      }
    }
  }
  
  def handleEjectUserRequest(msg: EjectUserFromVoiceRequest) {
//    println("Received eject user request uid=[" + msg.userID + "]")
    users.getUser(msg.userId) match {
      case Some(u) => {
        if (u.voiceUser.joined) {
          outGW.send(new EjectVoiceUser(meetingID, recorded, msg.ejectedBy, u.userID, voiceBridge, u.voiceUser.userId))
        }      
      }
      case None => // do nothing
    }
  }
   
      
  def handleSetLockSettings(msg: SetLockSettings) {
//    println("*************** Received new lock settings ********************")
    if (!permissionsEqual(msg.settings)) {
      newPermissions(msg.settings)
      val au = affectedUsers(msg.settings)
      outGW.send(new NewPermissionsSetting(meetingID, msg.setByUser, permissions, au))
      
      handleLockLayout(msg.settings.lockedLayout, msg.setByUser)
    }    
  }
    
  def handleInitLockSettings(msg: InitLockSettings) {
    if (! permissionsInited) {
      permissionsInited = true
      if (permissions != msg.settings || locked != msg.locked) {
	      permissions = msg.settings   
	      locked = msg.locked	    
	      val au = affectedUsers(msg.settings)
	      outGW.send(new PermissionsSettingInitialized(msg.meetingID, msg.locked, msg.settings, au))
      }      
    }
  }  

  def usersWhoAreNotPresenter():Array[UserVO] = {
    val au = ArrayBuffer[UserVO]()
    
    users.getUsers foreach {u =>
        if (! u.presenter) {
          au += u
        }
    }
    au.toArray    
  }
  
  def affectedUsers(settings: Permissions):Array[UserVO] = {
    val au = ArrayBuffer[UserVO]()
    
    users.getUsers foreach {u =>
      val nu = u.copy(permissions=permissions)
      users.addUser(nu)
        if (! u.presenter && u.role != Role.MODERATOR) {
          au += nu
        }
    }
    au.toArray
  }
  
  def handleUserRaiseHand(msg: UserRaiseHand) {
    users.getUser(msg.userId) foreach {user =>
      val uvo = user.copy(raiseHand=true)
      users.addUser(uvo)
      outGW.send(new UserRaisedHand(meetingID, recorded, uvo.userID))
    }
  }

  def handleUserLowerHand(msg: UserLowerHand) {
    users.getUser(msg.userId) foreach {user =>
      val uvo = user.copy(raiseHand=false)
      users.addUser(uvo)
      outGW.send(new UserLoweredHand(meetingID, recorded, uvo.userID, msg.loweredBy))
    }    
  }
  
  def handleEjectUserFromMeeting(msg: EjectUserFromMeeting) {
    users.getUser(msg.userId) foreach {user =>
      if (user.voiceUser.joined) {
        outGW.send(new EjectVoiceUser(meetingID, recorded, msg.ejectedBy, msg.userId, voiceBridge, user.voiceUser.userId))
      }
      
      users.removeUser(msg.userId)
      
      outGW.send(new UserEjectedFromMeeting(meetingID, recorded, msg.userId, msg.ejectedBy))
      outGW.send(new DisconnectUser(meetingID, msg.userId))
      
      outGW.send(new UserLeft(msg.meetingID, recorded, user))
    }    
  }

  def handleUserShareWebcam(msg: UserShareWebcam) {
    users.getUser(msg.userId) foreach {user =>
      val uvo = user.copy(hasStream=true, webcamStream=msg.stream)
      users.addUser(uvo)
      outGW.send(new UserSharedWebcam(meetingID, recorded, uvo.userID, msg.stream))
    }     
  }

  def handleUserunshareWebcam(msg: UserUnshareWebcam) {
    users.getUser(msg.userId) foreach {user =>
      val stream = user.webcamStream
      val uvo = user.copy(hasStream=false, webcamStream="")
      users.addUser(uvo)
      outGW.send(new UserUnsharedWebcam(meetingID, recorded, uvo.userID, stream))
    }     
  }
	                         
  def handleChangeUserStatus(msg: ChangeUserStatus):Unit = {    
		if (users.hasUser(msg.userID)) {
			  outGW.send(new UserStatusChange(meetingID, recorded, msg.userID, msg.status, msg.value))
		}  
  }
  
  def handleGetUsers(msg: GetUsers):Unit = {
	  outGW.send(new GetUsersReply(msg.meetingID, msg.requesterID, users.getUsers))
  }
  
  def handleUserJoin(msg: UserJoining):Unit = {
    val regUser = regUsers.get(msg.userID)
    regUser foreach { ru =>
      val vu = new VoiceUser(msg.userID, msg.userID, ru.name, ru.name,  
                           false, false, false, false)
      val uvo = new UserVO(msg.userID, ru.externId, ru.name, 
                  ru.role, raiseHand=false, presenter=false, 
                  hasStream=false, locked=false, webcamStream="", 
                  phoneUser=false, calledFromBbb=false,
                  vu, listenOnly=false, authToken=ru.authToken, pin=ru.pin, permissions)
  	
      logger.info("Web eser joined [" + uvo + "]")
	    users.addUser(uvo)
					
	    outGW.send(new UserJoined(meetingID, recorded, uvo))
	
	    outGW.send(new MeetingState(meetingID, recorded, uvo.userID, permissions, meetingMuted))
	    
	    // Become presenter if the only moderator		
	    if (users.numModerators == 1) {
	      if (ru.role == Role.MODERATOR) {
		      assignNewPresenter(msg.userID, ru.name, msg.userID)
	      }	  
	    }   
      webUserJoined
      startRecordingIfAutoStart()
    }
  }
			
  def handleUserLeft(msg: UserLeaving):Unit = {
     processUserLeft(msg.userID)
  }
  
  def processUserLeft(userId: String) {
	 if (users.hasUser(userId)) {
	  val user = users.removeUser(userId)
	  user foreach (u => outGW.send(new UserLeft(meetingID, recorded, u)))  
	  
    startCheckingIfWeNeedToEndVoiceConf()
	 }    
  }
  
  def updateVoiceUser(user: UserVO, muted: Boolean, talking: Boolean) = {     
     val vu = user.voiceUser
     
     val muteChanged = vu.muted != muted
     val talkingChanged = vu.talking != talking        
     
     logger.debug("Voice user status changed. vid=[" + vu.userId +"] conf=[" + voiceBridge 
         + "] muted[old,new]=[" + vu.muted + "," + muted
         + "] talking[old,new]=[" + vu.talking + "," + talking + "]");
         
     var nvu = vu.copy()
     if (vu.muted != muted) {
       nvu = nvu.copy(muted=muted)              
     }
            
     if (vu.talking != talking) {
       nvu = nvu.copy(talking=talking)
     }
     
     val nu = user.copy(voiceUser=nvu)
     users.addUser(nu)
     
     
     if (muteChanged) {
       logger.debug("Sending voice user muted. vid=[" + vu.userId +"] conf=[" + voiceBridge 
         + "] muted=[" + nvu.muted + "]");
       outGW.send(new UserVoiceMuted(meetingID, recorded, voiceBridge, nu))
     }
            
     if (talkingChanged) {
       logger.debug("Sending voice user is talking. vid=[" + vu.userId +"] conf=[" + voiceBridge 
         + "] talking=[" + nvu.talking + "]");
       outGW.send(new UserVoiceTalking(meetingID, recorded, voiceBridge, nu))
     }
  }
  
  def webUserJoinedVoiceConference(user: UserVO, voiceUserId: String, userId: String, callerName: String, callerNum: String, calledFromBbb: Boolean) {
     val nvu = new VoiceUser(voiceUserId, userId, callerName, callerNum,
                  true, false, false, false)
     val nu = user.copy(voiceUser=nvu, calledFromBbb=calledFromBbb)
     users.addUser(nu)
     logger.info("Web user joined voice conference. conf=[" + voiceBridge + "] vid=[" 
         + voiceUserId + "], wid=[" + user.userID + "] pin=[" + nu.pin + "] calledFromBbb=[" + nu.calledFromBbb + "]" )
     outGW.send(new UserJoinedVoice(meetingID, recorded, voiceBridge, nu))     
     if (meetingMuted)
        outGW.send(new MuteVoiceUser(meetingID, recorded, nu.userID, nu.userID, voiceBridge, nu.voiceUser.userId, meetingMuted))   
    stopAutoStartedRecording()
  }
  
  def nonWebUserJoinedVoiceConference(voiceUserId: String, callerName: String, callerNum: String, pin: String, calledFromBbb: Boolean) {
     // No current web user. This means that the user called in through
     // the phone. We need to generate a new user as we are not able
     // to match with a web user.
     val webUserId = users.generateWebUserId
     val vu = new VoiceUser(voiceUserId, webUserId, 
                            callerName, callerNum,
                            true, false, false, false)
     val uvo = new UserVO(webUserId, webUserId, callerName, 
		           Role.VIEWER, raiseHand=false, presenter=false, 
		           hasStream=false, locked=false, webcamStream="", 
		           phoneUser=true, calledFromBbb=calledFromBbb, 
		           vu, listenOnly=false, authToken=webUserId, pin=pin, permissions)
		  	
		 users.addUser(uvo)
		 logger.info("Phone caller joined voice conference. conf=[" + voiceBridge 
		     + "] wid=[" + webUserId + "] vid=[" + voiceUserId + "] pin=[" + pin + "] calledFromBbb=[" + uvo.calledFromBbb + "]")
		 outGW.send(new UserJoined(meetingID, recorded, uvo))
		      
		 outGW.send(new UserJoinedVoice(meetingID, recorded, voiceBridge, uvo))
		 if (meetingMuted)
        outGW.send(new MuteVoiceUser(meetingID, recorded, uvo.userID, uvo.userID, voiceBridge, uvo.voiceUser.userId, meetingMuted))    
  }
  
  def removePhoneCallerFromUsers(voiceUserId: String) {
    users.getUserWithVoiceUserId(voiceUserId) foreach {user =>
      val vu = new VoiceUser(user.userID, user.userID, user.name, user.name,  
                           false, false, false, false)
      val nu = user.copy(voiceUser=vu)
      users.addUser(nu)
            
      logger.info("Removing phone caller from users list. conf=[" + voiceBridge + "] vid=[" + voiceUserId + "] wid=[" + user.userID + "]" )
      outGW.send(new UserLeftVoice(meetingID, recorded, voiceBridge, nu))    
      
      if (user.phoneUser) {
	      processUserLeft(user.userID)       
      }
    }    
  }

  def ejectWebUserAsUserIsCallingInFromPhone(user: UserVO) {
    logger.info("Ejecting web user uid[=" + user.userID + "] vid=[" + user.voiceUser.userId + "] conf=[" + voiceBridge + "]." 
                         + "joined=[" + user.voiceUser.joined + "] phoneCalled=[" + user.phoneUser + "]")
    if (user.voiceUser.joined && user.calledFromBbb) {
      logger.info("Eject uid[=" + user.userID + "] vid=[" + user.voiceUser.userId + "] conf=[" + voiceBridge + "]. Calling in from phone." ) 
      outGW.send(new EjectVoiceUser(meetingID, recorded, user.userID, user.userID, voiceBridge, user.voiceUser.userId))
    }       
  }
    
  def ejectPhoneUserAsUserIsJoiningFromClient(user: UserVO) {
    logger.info("Ejecting Phone User uid[=" + user.userID + "] vid=[" + user.voiceUser.userId + "] conf=[" + voiceBridge + "]." 
                        + "joined=[" + user.voiceUser.joined + "] phoneCalled=[" + user.phoneUser + "]")
    if (user.voiceUser.joined && !user.calledFromBbb) {
      logger.info("Eject uid[=" + user.userID + "] vid=[" + user.voiceUser.userId + "] conf=[" + voiceBridge + "]. Calling in from client." ) 
      outGW.send(new EjectVoiceUser(meetingID, recorded, user.userID, user.userID, voiceBridge, user.voiceUser.userId))
    }       
  }
  
  def startRecordingVoiceConference() = {
    if (recorded && (users.numberOfUsersInVoiceConference == 1)) {
      logger.info("Request to start recording voice conference. mid=[" + meetingID + "] voiceConf=[" + voiceBridge + "]")
      val filename = meetingID + "-" + TimestampGenerator.generateTimestamp
      outGW.send(new StartRecordingVoiceConference(meetingID, recorded, voiceBridge, filename))
    }
  }

  def stopRecordingVoiceConference() = {
    if (recorded && (users.numberOfUsersInVoiceConference == 0)) {
      logger.info("Request to stop recording voice conference. mid=[" + meetingID + "] voiceConf=[" + voiceBridge + "]")
      outGW.send(new StopRecordingVoiceConference(meetingID, recorded, voiceBridge))
    }      
  }
    
  def handleVoiceUserStatusChangedMessage(msg: VoiceUserStatusChangedMessage) = {
    users.getUserWithVoiceUserId(msg.voiceUserId) match {
      case Some(user) => {
        if (user.pin == msg.authCode) {
          updateVoiceUser(user, msg.muted, msg.talking)
        } else {
          users.getUserWithAuthCode(msg.authCode) match {
            case Some(userWithAuthCode) => {
              ejectWebUserAsUserIsCallingInFromPhone(userWithAuthCode)
              logger.info("Web user joined voice conference using phone. pin=[" 
                  + msg.authCode + "], vid=[" + msg.voiceUserId + "], " 
                  + "conf=[" + msg.voiceConf + "], wid=[" + userWithAuthCode.userID + "]")
          		removePhoneCallerFromUsers(msg.voiceUserId)         
              webUserJoinedVoiceConference(userWithAuthCode, msg.voiceUserId, 
                  userWithAuthCode.userID, msg.callerIdName, msg.callerIdNum, 
                  calledFromBbb=false)
              startRecordingVoiceConference()
            }
            case None => logger.info("Cannot find web user with pin=[" + msg.authCode + "]")
          }
        }
      }  
      case None => {
        if (msg.calledFromBbb) {
          users.getUserWithAuthCode(msg.authCode) match {
            case Some(userWithAuthCode) => {
              ejectPhoneUserAsUserIsJoiningFromClient(userWithAuthCode)
              logger.info("Web user joined voice conference from client. pin=[" 
                  + msg.authCode + "], vuid=[" + msg.voiceUserId + "], " 
                  + "conf=[" + msg.voiceConf + "], wid=[" 
                  + userWithAuthCode.userID + "]")
              webUserJoinedVoiceConference(userWithAuthCode, msg.voiceUserId, 
                  userWithAuthCode.userID, msg.callerIdName, msg.callerIdNum, 
                  calledFromBbb=true)
              startRecordingVoiceConference()
            }
            case None => logger.info("Cannot find web user with pin=[" + msg.authCode + "]")
          }
        } else {
          nonWebUserJoinedVoiceConference(msg.voiceUserId, msg.callerIdName, msg.callerIdNum, msg.authCode, calledFromBbb=false)
          startRecordingVoiceConference()
        }
      }
    }    
  }
  
  
	def handleVoiceUserLeftConfMessage(msg: VoiceUserLeftConfMessage) = {
    users.getUserWithVoiceUserId(msg.voiceUserId) foreach {user =>
      val vu = new VoiceUser(user.userID, user.userID, user.name, user.name,  
                           false, false, false, false)
      val nu = user.copy(voiceUser=vu)
      users.addUser(nu)
            
      logger.info("Received voice user left. conf=[" + voiceBridge + "] vid=[" + msg.voiceUserId + "] uid=[" + user.userID + "]" )
      outGW.send(new UserLeftVoice(meetingID, recorded, voiceBridge, nu))    
      
      if (user.phoneUser) {
        logger.info("Phone caller leaving. conf=[" + voiceBridge + "] vid=[" + msg.voiceUserId + "] uid=[" + user.userID + "]" )
	      if (users.hasUser(user.userID)) {
	        logger.info("Removing phone caller from users. conf=[" + voiceBridge + "] vid=[" + msg.voiceUserId + "] uid=[" + user.userID + "]" )
	        processUserLeft(user.userID)
	      }        
      }
      
      stopRecordingVoiceConference()
    }   
	}
	    
  def handleVoiceUserJoined(msg: VoiceUserJoined) = {
      val user = users.getUser(msg.voiceUser.webUserId) match {
        case Some(user) => {
          val nu = user.copy(voiceUser=msg.voiceUser)
          users.addUser(nu)
          logger.info("Received user joined voice for user [" + nu.name + "] userid=[" + msg.voiceUser.webUserId + "]" )
          outGW.send(new UserJoinedVoice(meetingID, recorded, voiceBridge, nu))
          
          if (meetingMuted)
            outGW.send(new MuteVoiceUser(meetingID, recorded, nu.userID, nu.userID, voiceBridge, nu.voiceUser.userId, meetingMuted))
        }
        case None => {
          // No current web user. This means that the user called in through
          // the phone. We need to generate a new user as we are not able
          // to match with a web user.
          val webUserId = users.generateWebUserId
          val vu = new VoiceUser(msg.voiceUser.userId, webUserId, 
                                 msg.voiceUser.callerName, msg.voiceUser.callerNum,
                                 true, false, false, false)
          val uvo = new UserVO(webUserId, webUserId, msg.voiceUser.callerName, 
		                  Role.VIEWER, raiseHand=false, presenter=false, 
		                  hasStream=false, locked=false, webcamStream="", 
		                  phoneUser=true, calledFromBbb=false,
		                  vu, listenOnly=false, authToken=webUserId, pin=webUserId, permissions)
		  	
		      users.addUser(uvo)
		      logger.info("New user joined voice for user [" + uvo.name + "] userid=[" + msg.voiceUser.webUserId + "]")
		      outGW.send(new UserJoined(meetingID, recorded, uvo))
		      
		      outGW.send(new UserJoinedVoice(meetingID, recorded, voiceBridge, uvo))
		      if (meetingMuted)
            outGW.send(new MuteVoiceUser(meetingID, recorded, uvo.userID, uvo.userID, voiceBridge, uvo.voiceUser.userId, meetingMuted))
        }
      }
  }
  
  def handleVoiceUserLeft(msg: VoiceUserLeft) {
    users.getUser(msg.userId) foreach {user =>
      val vu = new VoiceUser(user.userID, user.userID, user.name, user.name,  
                           false, false, false, false)
      val nu = user.copy(voiceUser=vu)
      users.addUser(nu)
            
//      println("Received voice user left =[" + user.name + "] wid=[" + msg.userId + "]" )
      outGW.send(new UserLeftVoice(meetingID, recorded, voiceBridge, nu))    
      
      if (user.phoneUser) {
	      if (users.hasUser(user.userID)) {
	        val userLeaving = users.removeUser(user.userID)
	        userLeaving foreach (u => outGW.send(new UserLeft(msg.meetingID, recorded, u)))
	      }        
      }
    }    
  }
  
  def handleVoiceUserMuted(msg: VoiceUserMuted) {
    users.getUser(msg.userId) foreach {user =>
      val talking = if (msg.muted) false else user.voiceUser.talking
      val nv = user.voiceUser.copy(muted=msg.muted, talking=talking)
      val nu = user.copy(voiceUser=nv)
      users.addUser(nu)
//      println("Received voice muted=[" + msg.muted + "] wid=[" + msg.userId + "]" )
      outGW.send(new UserVoiceMuted(meetingID, recorded, voiceBridge, nu))        
    }   
  }
  
  def handleVoiceUserTalking(msg: VoiceUserTalking) {
    users.getUser(msg.userId) foreach {user =>
      val nv = user.voiceUser.copy(talking=msg.talking)
      val nu = user.copy(voiceUser=nv)
      users.addUser(nu)
//      println("Received voice talking=[" + msg.talking + "] wid=[" + msg.userId + "]" )
      outGW.send(new UserVoiceTalking(meetingID, recorded, voiceBridge, nu))        
    }     
  }
  
  def handleAssignPresenter(msg: AssignPresenter):Unit = {
	assignNewPresenter(msg.newPresenterID, msg.newPresenterName, msg.assignedBy)
  } 
	
  def assignNewPresenter(newPresenterID:String, newPresenterName: String, assignedBy: String) {
    if (users.hasUser(newPresenterID)) {

      users.getCurrentPresenter match {
        case Some(curPres) => {
  	      users.unbecomePresenter(curPres.userID)  
  	      outGW.send(new UserStatusChange(meetingID, recorded, curPres.userID, "presenter", false:java.lang.Boolean))        
        }
        case None => // do nothing
      }
      
  	  users.getUser(newPresenterID) match {
  	    case Some(newPres) => {
  	      users.becomePresenter(newPres.userID)      	  
  	      currentPresenter = new Presenter(newPresenterID, newPresenterName, assignedBy)
  	      outGW.send(new PresenterAssigned(meetingID, recorded, new Presenter(newPresenterID, newPresenterName, assignedBy)))
          outGW.send(new UserStatusChange(meetingID, recorded, newPresenterID, "presenter", true:java.lang.Boolean))  	      
  	    }
  	    case None => // do nothing
  	  }

    }
  }
}
