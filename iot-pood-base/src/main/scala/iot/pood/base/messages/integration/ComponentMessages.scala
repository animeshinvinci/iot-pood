package iot.pood.base.messages.integration

import akka.actor.ActorRef

/**
  * Created by rafik on 22.9.2017.
  */
object ComponentMessages {

    case class GetIntegrationComponentRequest(sender: ActorRef)

    case class GetIntegrationComponentResponse(actorRef: ActorRef)

}
