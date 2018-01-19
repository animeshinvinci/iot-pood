package iot.pood.management.actors.device

import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.management.actors.device.DeviceActor.{Create, DeviceDeleted}
import iot.pood.management.dto.DeviceDto.{CreateDevice, UpdateDevice}
import iot.pood.management.model.DeviceModel.Device

object DeviceActor {

  def props:Props = Props(new DeviceActor)


  sealed trait DeviceMessage

  case class Create(device: CreateDevice) extends DeviceMessage
  case class CreateWithGroup(device: CreateWithGroup) extends DeviceMessage
  case class Update(id: String,device: UpdateDevice) extends DeviceMessage
  case class Delete(id: String) extends DeviceMessage

  case class DeviceCreated(device: Device) extends DeviceMessage
  case class DeviceUpdated(device: Device) extends DeviceMessage
  object DeviceDeleted extends DeviceMessage

}

class DeviceActor extends BaseActor {


  override def receive = {
    case Create(device) => sender() ! "xxx"
  }
}

