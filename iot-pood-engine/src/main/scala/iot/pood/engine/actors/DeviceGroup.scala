package iot.pood.engine.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.concurrent.duration.DurationLong

/**
  * Created by rafik on 14.7.2017.
  */
object DeviceGroup {

  def props(groupId: String)=  Props(new DeviceGroupActor(groupId))

  final case class RequestDeviceList(requestId: Long)

  final case class ReplyDeviceList(requestId: Long, ids: Set[String])

  final case class RequestAllData(requestId: Long)
  final case class RespondAllData(requestId: Long, data: Map[String, DataReading])

  sealed trait DataReading
  final case class DeviceData(value: Option[String]) extends DataReading
  case object DataNotAvailable extends DataReading
  case object DeviceNotAvailable extends DataReading
  case object DeviceTimedOut extends DataReading

}

class DeviceGroupActor(groupId: String) extends Actor with ActorLogging{

  import DeviceManager._
  import DeviceGroup._

  var deviceIdToActor = Map.empty[String,ActorRef]
  var actorToDeviceId = Map.empty[ActorRef, String]

  override def receive: Receive = {

    case message @ RequestRegisterDevice(`groupId`,_) => {
        deviceIdToActor.get(message.deviceId) match {
          case Some(ref) =>
            log.info("Device is actually mapped: {}. Forward message",message.deviceId)
            ref forward message
          case None => {
            log.info("Create new device actor for: {}",message.deviceId)
            val deviceActor = context.actorOf(Device.props(groupId,message.deviceId))
            context.watch(deviceActor)
            deviceIdToActor += message.deviceId -> deviceActor
            actorToDeviceId += deviceActor -> message.deviceId
            deviceActor forward message
          }
        }
     }

    case RequestDeviceList(requestId) =>
      sender() ! ReplyDeviceList(requestId, deviceIdToActor.keySet)

    case RequestRegisterDevice(groupId,deviceId) => {
        log.warning("Ignore: {} This device group is responsible for group: {}", groupId, this.groupId)
      }

    case RequestAllData(requestId) =>
      context.actorOf(DeviceGroupQuery.props(actorToDeviceId = actorToDeviceId,
        requestId = requestId,sender(),3.seconds))
    }
}
