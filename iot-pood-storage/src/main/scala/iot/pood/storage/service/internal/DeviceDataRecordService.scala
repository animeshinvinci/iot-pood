package iot.pood.storage.service.internal

import com.outworkers.phantom.ResultSet
import com.outworkers.phantom.dsl._
import iot.pood.storage.entity.CassandraEntity.DeviceDataRecord
import iot.pood.storage.service.{DataService, StorageCreator}

import scala.concurrent.Future

/**
  * Created by rafik on 4.8.2017.
  */
trait DeviceDataRecordService extends StorageCreator with DataService[DeviceDataRecord]{

  def saveItem(record: DeviceDataRecord): Future[ResultSet] = {
    database.DeviceDataRecord.storeRecord(record)
  }

  def saveItems(records: Set[DeviceDataRecord]): Future[Set[ResultSet]] = {
    database.DeviceDataRecord.storeRecords(records)
  }
}

object DeviceDataRecordService extends DeviceDataRecordService
