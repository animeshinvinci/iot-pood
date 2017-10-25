package iot.pood.management.repository

import iot.pood.management.dao.Dao
import iot.pood.management.dao.internal.{MongoDao, MongoRepositoryCreator}
import iot.pood.management.model.DeviceModel._

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 25.10.2017.
  */
trait DeviceDaoRepository {

  def deviceDao: Dao[Device,String]

  def dashBoardDao: Dao[DashBoard,String]

  def deviceGroupDao: Dao[DeviceGroup,String]

}

object DeviceDaoRepository extends DeviceDaoRepository with DeviceRepositoryCreator{

  override def deviceDao: Dao[Device, String] = DeviceRepository

  override def dashBoardDao: Dao[DashBoard, String] = DashBoardRepository

  override def deviceGroupDao: Dao[DeviceGroup, String] = DeviceGroupRepository
}


trait DeviceRepositoryCreator extends MongoRepositoryCreator with DeviceModelConverter {

  import ExecutionContext.Implicits.global

  object DeviceRepository extends MongoDao[Device,String]("devices")

  object DashBoardRepository extends MongoDao[DashBoard,String]("dashboards")

  object DeviceGroupRepository extends MongoDao[DeviceGroup,String]("deviceGroups")

}




