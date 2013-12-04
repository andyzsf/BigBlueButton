package org.bigbluebutton.apps
import org.bigbluebutton.apps.models.MeetingSession
import akka.actor.ActorRef
import akka.actor.ActorContext
import org.bigbluebutton.apps.models.MeetingConfig

class Meeting(val session: MeetingSession, 
              val pubsub: ActorRef,
              val config: MeetingConfig)
              (implicit val context: ActorContext) {
  
  val actorRef = context.actorOf(MeetingActor.props(pubsub, session, config), session.session)
  
  val configs = new collection.immutable.HashMap[String, String]()
}