package iot.pood.management.dao.connector

import java.util.Date

import iot.pood.base.log.Log
import iot.pood.management.dao.internal.MongoDao
import iot.pood.management.model.BaseModel.ObjectStatus
import iot.pood.management.model.DeviceModel.Device
import iot.pood.management.model.UserModel.{ConfigParameter, LogRecord, User}
import iot.pood.management.repository.{DeviceDaoRepository, UserDaoRepository}
import org.joda.time.DateTime
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, BSONObjectID, Macros}
import sun.util.calendar.Gregorian.Date

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by rafik on 18.10.2017.
  */
object MongoDbConnector  extends App with Log {

  import ExecutionContext.Implicits.global


  val deviceDao = DeviceDaoRepository.deviceDao

  val logDao = UserDaoRepository.logDao

  val device = Device(None, new DateTime(), "1.1", ObjectStatus.active, Map(), Map())

  deviceDao.save(device).onComplete(result => result match {
    case Success(r) => log.info("Success: {}", r)
    case Failure(e) => log.error("Error: {}", e)
  })
}
