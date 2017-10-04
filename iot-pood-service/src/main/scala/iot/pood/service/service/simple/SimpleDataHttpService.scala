package iot.pood.service.service.simple

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Route
import iot.pood.base.http.service.internal.BaseHttpService
import iot.pood.base.log.Log
import iot.pood.service.SimpleActor
import iot.pood.service.service.ApiV1

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 3.10.2017.
  */

object SimpleDataHttpService extends Log{

  def apply()(implicit executionContext: ExecutionContext,actorSystem: ActorSystem)  = {

    log.info("Create simple data HTTP service")
    val serviceActor = actorSystem.actorOf(Props(new SimpleActor))
    new SimpleDataHttpService(serviceActor)
  }
}

class SimpleDataHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends BaseHttpService(serviceWorker)
  with ApiV1{

  override def route: Route = join(switchState,numberValue)

    val switchState: Route = {
      pathPrefix("data"/"state"/"switch"){
        path("on"){
          complete("ok")
        } ~ path("off"){
          throw new Exception("problem just test")
        }
      }
    }

    val numberValue: Route = {
      pathPrefix("data"/"value"){
        path(IntNumber){  number => {
            complete(s"ok: $number")
          }
        } ~ path(DoubleNumber){ number =>{
            complete(s"ok: $number")
          }
        }
      }
    }
}
