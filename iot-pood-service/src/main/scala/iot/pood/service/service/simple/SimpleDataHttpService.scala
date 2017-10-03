package iot.pood.service.service.simple

import akka.actor.ActorRef
import akka.http.scaladsl.server.Route
import iot.pood.base.http.service.internal.BaseHttpService

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 3.10.2017.
  */

object SimpleDataHttpService{
  def apply(serviceWorker: ActorRef)(implicit executionContext: ExecutionContext): SimpleDataHttpService = new SimpleDataHttpService(serviceWorker)
}

class SimpleDataHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends BaseHttpService(serviceWorker){

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
