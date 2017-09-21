package iot.pood.storage.service

import iot.pood.storage.db.database.DataRecordProvider
import scala.concurrent.duration.DurationInt
import com.outworkers.phantom.dsl.context

/**
  * Storage creator. First after application start.
  * Created by rafik on 9.8.2017.
  */
trait StorageCreator extends DataRecordProvider{


  /**
    * Check if storage is created. If not create one
    *
    */
  def create() = database.create(5 seconds)

}
