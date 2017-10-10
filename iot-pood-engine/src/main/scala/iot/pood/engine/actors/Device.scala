package iot.pood.engine.actors

import akka.actor.{Actor, ActorLogging, Props}
import iot.pood.engine.actors.DeviceManager.RequestRegisterDevice
/**
  * Factory and messages send to device actor
  *
  * Created by rafik on 14.7.2017.
  */
object Device {

  def  props(groupId:String , deviceId: String) = Props(new DeviceActor(groupId,deviceId))

  sealed  trait DeviceRequest{
    def requestId: Long
  }

  case class RecordData(requestId:Long, data: String) extends DeviceRequest

  case class DataRecorded(requestId:Long) extends DeviceRequest

  case class ReadData(requestId: Long) extends DeviceRequest

  case class ReadDataResponse(requestId: Long, data:Option[String]) extends DeviceRequest

}

/**
  * Device actor, represent one IoT device
  *
  * @param groupId
  * @param deviceId
  */
class DeviceActor(groupId:String ,deviceId:String) extends Actor with ActorLogging {

  import Device._
  import DeviceGroup._
  import DeviceManager._

  var actualValue: Option[String] = None


  override def receive = {
    case RequestRegisterDevice(`groupId`, `deviceId`) =>
      sender() ! DeviceRegistered

    case RequestRegisterDevice(groupId, deviceId) =>
      log.warning(
        "Ignoring TrackDevice request for {}-{}.This actor is responsible for {}-{}.",
        groupId, deviceId, this.groupId, this.deviceId
      )
    case RecordData(requestId,data) => {
      log.info("Device: {} data: {}",deviceId,data)
      actualValue = Some(data)
      sender() ! DataRecorded(requestId)
    }

    case ReadData(requestId) => {
      log.info("Read data request: {}",requestId)
      sender() ! ReadDataResponse(requestId,actualValue)
    }

  }

  override def preStart() = {
    log.info("Pre Start")
  }

  override def postStop() = {
    log.info("Post Stop")
  }
}

