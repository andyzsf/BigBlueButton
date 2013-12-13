package org.bigbluebutton.apps
import org.bigbluebutton.apps.models.Session
import akka.actor.ActorRef
import akka.actor.ActorContext
import org.bigbluebutton.apps.models.MeetingDescriptor

object RunningMeeting {  
  def apply(session: Session, pubsub: ActorRef, config: MeetingDescriptor)
              (implicit context: ActorContext) = 
                new RunningMeeting(session, pubsub, config)(context)
}

class RunningMeeting (val session: Session, 
              val pubsub: ActorRef,
              val config: MeetingDescriptor)
              (implicit val context: ActorContext) {
  
  val actorRef = context.actorOf(RunningMeetingActor.props(pubsub, session, config), session.id)
  
  val configs = new collection.immutable.HashMap[String, String]()
}