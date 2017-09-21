package iot.pood.storage.actor

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import com.outworkers.phantom.ResultSet
import iot.pood.storage.actor.Storage.{DataNotSaved, DataSaved, SaveDeviceDataRecord}
import iot.pood.storage.entity.CassandraEntity._
import iot.pood.storage.service.DataService
import akka.pattern.pipe
import Storage._
import akka.remote.ContainerFormats.ActorRef
import com.outworkers.phantom.ResultSet
import iot.pood.base.actors.BaseActor
import iot.pood.storage.service.internal.{DeviceDataRecordService, DeviceStateDataRecordService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
  * Actor responsible for saving DeviceDataRecord
  * and DeviceStateDataRecord
  *
  * Created by rafik on 9.8.2017.
  */
object Storage {


  def props() = Props(new StorageActor)

  sealed trait StorageRequest {
    def requestId: Long
  }

  case class SaveDeviceDataRecord(requestId: Long, record: DeviceDataRecord) extends StorageRequest

  case class SaveDeviceStateDateRecord(requestId: Long, record: DeviceStateDataRecord) extends StorageRequest

  case class DataSaved(requestId: Long) extends StorageRequest

  case class DataNotSaved(requestId: Long) extends StorageRequest

  case class UnableToSaveData(requestId: Long) extends StorageRequest

  trait StorageHandler extends BaseActor {

    def save[A](storage: DataService[A], request: StorageRequest, value: A) = {
      log.info("Save value: {}", value)
      storage.saveItem(value).map(result => {
        if (result.wasApplied()) {
          log.info("Data: {} successfully saved", value)
          sender() ! DataSaved(request.requestId)
        } else {
          log.error("Data: {} wasn't saved successfully", value)
          sender() ! DataNotSaved(request.requestId)
        }
      }).recover({
        case e @ _ => {
          log.error("Unable to save data: {}", e)
          sender() ! DataNotSaved(request.requestId)
        }
      })
    }
  }

}

class StorageActor extends StorageHandler{

  def receive: Receive = {
    case request @ SaveDeviceStateDateRecord(requestId, record) => {
      log.info("Save state of device: {}",record.deviceId)
      save(DeviceStateDataRecordService,request,record)
    }
    case request @ SaveDeviceDataRecord(requestId,record) => {
      log.info("Save data from device: {}",record.deviceId)
      save(DeviceDataRecordService,request,record)
    }
    case _ =>{
      sender() ! UnableToSaveData
    }
  }
}







