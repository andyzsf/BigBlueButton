package org.bigbluebutton.core

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Props
import scala.collection.mutable.HashMap
import org.bigbluebutton.core.api._
import org.bigbluebutton.apps.RunningMeeting

object BigBlueButtonActor {
  	def props(outGW: MessageOutGateway): Props =  Props(classOf[BigBlueButtonActor], outGW)
}

class BigBlueButtonActor(outGW: MessageOutGateway) extends Actor {

  private var meetings = new HashMap[String, RunningMeeting]
  
  def receive = {
	      case msg: CreateMeeting                 => handleCreateMeeting(msg)
	      case msg: DestroyMeeting                => handleDestroyMeeting(msg)
	      case msg: KeepAliveMessage              => handleKeepAliveMessage(msg)
	      case msg: InMessage                     => handleMeetingMessage(msg)
	      case _ => // do nothing
  }
  
  private def handleMeetingMessage(msg: InMessage):Unit = {
    meetings.get(msg.meetingID) match {
      case None => //
      case Some(m) => {
       // log.debug("Forwarding message [{}] to meeting [{}]", msg.meetingID)
        m.actorRef forward msg
      }
    }
  }

  private def handleKeepAliveMessage(msg: KeepAliveMessage):Unit = {
    outGW.send(new KeepAliveMessageReply(msg.aliveID))
  }
    
  private def handleDestroyMeeting(msg: DestroyMeeting) {
    println("****************** BBBActor received DestroyMeeting message for meeting id [" + msg.meetingID + "] **************")
    meetings.get(msg.meetingID) match {
      case None => println("Could not find meeting id[" + msg.meetingID + "] to destroy.")
      case Some(m) => {
        m.actorRef ! StopMeetingActor
        meetings -= msg.meetingID    
        println("Kinc everyone out on meeting id[" + msg.meetingID + "].")
        outGW.send(new EndAndKickAll(msg.meetingID, m.recorded))
        
        println("Destroyed meeting id[" + msg.meetingID + "].")
        outGW.send(new MeetingDestroyed(msg.meetingID))
      }
    }
  }
  
  private def handleCreateMeeting(msg: CreateMeeting):Unit = {
    meetings.get(msg.meetingID) match {
      case None => {
    	  var m = RunningMeeting(msg.meetingID, msg.meetingName, msg.recorded, msg.voiceBridge, msg.duration, outGW)
    	  meetings += m.meetingID -> m
    	  outGW.send(new MeetingCreated(m.meetingID, m.recorded, m.voiceBridge))
    	  
    	  m.actorRef ! new InitializeMeeting(m.meetingID, m.recorded)
      }
      case Some(m) => // do nothing
    }
  }
  
}
