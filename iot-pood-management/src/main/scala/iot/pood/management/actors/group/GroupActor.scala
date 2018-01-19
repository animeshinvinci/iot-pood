package iot.pood.management.actors.group

import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.management.actors.group.GroupActor.CreateGroup

object GroupActor {

  def props() = Props(new GroupActor)

  sealed trait GroupMessage

  case class CreateGroup(status: String) extends GroupMessage
  case class UpdateGroup(status: String) extends GroupMessage

}

class GroupActor extends BaseActor {
  override def receive = {
    case CreateGroup => log.info("Create group")
  }
}

