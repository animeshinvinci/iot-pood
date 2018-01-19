package iot.pood.base.http.health

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Route
import iot.pood.base.actors.BaseActor
import iot.pood.base.api.ApiV1
import iot.pood.base.http.base.internal.BaseHttpService
import iot.pood.base.log.Log

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 8.11.2017.
  */
object HealthHttpService extends Log{

  def apply()(implicit executionContext: ExecutionContext,actorSystem: ActorSystem)  = {

    log.info("Create HEALTH service")
    val serviceActor = actorSystem.actorOf(Props(new SimpleWorker))
    new HealthHttpService(serviceActor)
  }

}

class HealthHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends BaseHttpService
with ApiV1
{
  override def route: Route = {

    pathPrefix("health"/"check"){
      complete("OK")
    }
  }
}


class SimpleWorker extends BaseActor {
  override def receive: Receive = {
    case _ => log.info("Accept message:")
  }
}

