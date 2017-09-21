package iot.pood.storage.service.internal

import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.dsl._
import iot.pood.storage.entity.CassandraEntity.{DeviceDataRecord, DeviceStateDataRecord}
import iot.pood.storage.service.{DataService, StorageCreator}

import scala.concurrent.Future

/**
  * Created by rafik on 4.8.2017.
  */
trait DeviceStateDataRecordService extends StorageCreator with DataService[DeviceStateDataRecord]{

  def saveItem(record: DeviceStateDataRecord): Future[ResultSet] = {
    database.DeviceStateDataRecord.storeRecord(record)
  }

  def saveItems(records: Set[DeviceStateDataRecord]): Future[Set[ResultSet]] = {
    database.DeviceStateDataRecord.storeRecords(records)
  }

}

object DeviceStateDataRecordService extends DeviceStateDataRecordService
