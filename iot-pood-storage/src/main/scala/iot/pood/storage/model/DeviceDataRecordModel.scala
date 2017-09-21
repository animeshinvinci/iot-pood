package iot.pood.storage.model


import iot.pood.storage.entity.CassandraEntity._
import com.outworkers.phantom.dsl._

/**
  *
  * Created by rafik on 1.8.2017.
  */
abstract class DeviceDataRecordModel extends Table[DeviceDataRecordModel,DeviceDataRecord] {

  override def tableName: String = "device_data_record"

  object deviceId extends StringColumn with PartitionKey{
    override def name: String = "device_id"
  }

  object created extends DateColumn with ClusteringOrder with Ascending

  object dataId extends StringColumn {
    override def name: String = "data_id"
  }

  object data extends StringColumn

}

/**
  *
  *  case class DeviceStateDataRecord(deviceId: String,
                                   created: Date,
                                   state: String) extends DataRecord
  *
  */
abstract class DeviceStateDataRecordModel extends Table[DeviceStateDataRecordModel,DeviceStateDataRecord]
{
  override def tableName: String = "device_state_data_record"

  object deviceId extends StringColumn with PartitionKey{
    override def name: String = "device_id"
  }

  object created extends DateColumn with PartitionKey

  object state extends StringColumn

}




