package org.bigbluebutton.apps
import org.bigbluebutton.apps.models.MeetingSession
import akka.actor.ActorRef
import akka.actor.ActorContext
import org.bigbluebutton.apps.models.MeetingDescriptor
import org.bigbluebutton.apps.models.MeetingDescriptor

object Meeting {
  
  case class CreateMeeting(name: String, externalId: String, descriptor: MeetingDescriptor)
  case class CreateMeetingResponse(success: Boolean, 
                                   descriptor: MeetingDescriptor, 
                                   error: Option[String] = None, 
                                   session: Option[MeetingSession])
  
  def apply(session: MeetingSession, 
              pubsub: ActorRef,
              config: MeetingDescriptor)
              (implicit context: ActorContext) = 
                new Meeting(session, pubsub, config)(context)
}

class Meeting private (val session: MeetingSession, 
              val pubsub: ActorRef,
              val config: MeetingDescriptor)
              (implicit val context: ActorContext) {
  
  val actorRef = context.actorOf(MeetingActor.props(pubsub, session, config), session.session)
  
  val configs = new collection.immutable.HashMap[String, String]()
}