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

object DeviceDaoRepository extends DeviceDaoRepository{

  override def deviceDao: Dao[Device, String] = DeviceRepositoryCreator.DeviceRepository

  override def dashBoardDao: Dao[DashBoard, String] = DeviceRepositoryCreator.DashBoardRepository

  override def deviceGroupDao: Dao[DeviceGroup, String] = DeviceRepositoryCreator.DeviceGroupRepository
}


trait DeviceRepositoryCreator extends MongoRepositoryCreator with DeviceModelConverter {

  import ExecutionContext.Implicits.global

  object DeviceRepository extends MongoDao[Device,String]("devices")

  object DashBoardRepository extends MongoDao[DashBoard,String]("dashboards")

  object DeviceGroupRepository extends MongoDao[DeviceGroup,String]("deviceGroups")

}

object DeviceRepositoryCreator extends DeviceRepositoryCreator




