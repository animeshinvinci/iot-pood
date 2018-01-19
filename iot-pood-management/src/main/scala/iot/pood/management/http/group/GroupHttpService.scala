package iot.pood.management.http.group

import akka.actor.{ActorRef, ActorSystem, Props}
import iot.pood.base.api.ApiV1
import iot.pood.base.http.base.internal.BaseHttpService
import iot.pood.base.log.Log
import iot.pood.management.actors.group.GroupActor

import scala.concurrent.ExecutionContext

object GroupHttpService extends Log{

  def apply()(implicit executionContext: ExecutionContext,actorSystem: ActorSystem)  = {

    log.info("Create GROUP service")
    val groupActor = actorSystem.actorOf(GroupActor.props())
    new GroupHttpService(groupActor)
  }

}

class GroupHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends BaseHttpService
  with ApiV1 {

  override def route = pathPrefix("group"){
    group ~
    groupDevices ~
    groupConfigurations ~
    createGroup ~
    deleteGroup ~
    update
  }

  val group = get{
    complete("ok get")
  }

  val groupDevices = get{
    pathPrefix("devices") {
      complete("group devices OK")
    }
  }

  val groupConfigurations = get {
    pathPrefix("config") {
      complete("group config ok")
    }
  }

  val createGroup = post {
    complete("ok")
  }

  val deleteGroup = delete {
    complete("ok")
  }

  val update = put{
    complete("ok")
  }



}

