package iot.pood.storage.db.database

import iot.pood.storage.db.connector.Connector._
import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.dsl._
import iot.pood.storage.db.connector.Connector
import iot.pood.storage.model.{DeviceDataRecordModel, DeviceStateDataRecordModel}

/**
  * Created by rafik on 4.8.2017.
  */
class DataRecordDatabase(override val connector: CassandraConnection) extends Database[DataRecordDatabase](connector){

  object DeviceDataRecord extends DeviceDataRecordModel with Connector

  object DeviceStateDataRecord extends DeviceStateDataRecordModel with Connector

}
object ProductionDb extends DataRecordDatabase(Connector.connector)

trait DataRecordProvider extends DatabaseProvider[DataRecordDatabase] {

  override def database: DataRecordDatabase = ProductionDb
}

