package iot.pood.storage.entity

import java.util.Date

import com.outworkers.phantom.dsl.UUID
import org.joda.time.DateTime

/**
  * Created by rafik on 1.8.2017.
  */
object CassandraEntity {

  trait Unique{
    def deviceId(): String
  }

  trait DataRecord extends Unique{

    def created(): Date

  }

  case class DeviceDataRecord(deviceId: String,
                              created: Date,
                              dataId: String,
                              data: String) extends DataRecord

  case class DeviceStateDataRecord(deviceId: String,
                                   created: Date,
                                   state: String) extends DataRecord

}
