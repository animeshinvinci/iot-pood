package iot.pood.management.repository

import iot.pood.base.log.Log
import iot.pood.management.dao.Dao
import iot.pood.management.dao.internal.{MongoDao, MongoRepositoryCreator}
import iot.pood.management.model.DeviceModel.{DashBoard, Device, DeviceGroup, DeviceModelConverter}
import iot.pood.management.model.UserModel._

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 25.10.2017.
  */
trait UserDaoRepository {

  def userDao: Dao[User,String]

  def tokenDao: Dao[Token,String]

  def logDao: Dao[LogRecord,String]

}

object UserDaoRepository extends UserDaoRepository with UserRepositoryCreator{

  override def userDao: Dao[User, String] = UserRepository

  override def tokenDao: Dao[Token, String] = TokenRepository

  override def logDao: Dao[LogRecord, String] = LogRepository

}


trait UserRepositoryCreator extends MongoRepositoryCreator with ConverterImplicit {

  import ExecutionContext.Implicits.global

  object UserRepository extends MongoDao[User,String]("users")

  object TokenRepository extends MongoDao[Token,String]("tokens")

  object LogRepository extends MongoDao[LogRecord,String]("logs")

}

