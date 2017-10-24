package iot.pood.management.dao.connector

import java.util.Date

import iot.pood.base.log.Log
import iot.pood.management.dao.Dao
import iot.pood.management.dao.Results.{DaoResult, SuccessDaoResult}
import iot.pood.management.dao.internal.MongoDao
import iot.pood.management.model.UserModel.{ConfigParameter, LogRecord, User}
import iot.pood.management.repository.ManagementRepository
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

  val userDao = ManagementRepository.userDao

  val logDao = ManagementRepository.logDao

  val parameter = Map("1"->ConfigParameter("1","sdfsdf"),"2"->ConfigParameter("2","sdfsdf"))
  val user = User(BSONObjectID.generate.stringify,"loginxxx","emailxxx","paswordxx","status",new Date(0l),parameter)


  val saveLog =  LogRecord(BSONObjectID.generate.stringify,new Date(2l),"sdf","user","this is log")

  userDao.save(user).onComplete(result => result match {
    case Success(r) => log.info("Success: {}",r)
    case Failure(e) => log.error("Error: {}",e)
  })

  logDao.save(saveLog).onComplete(result => result match {
    case Success(r) => log.info("Success: {}",r)
    case Failure(e) => log.error("Error: {}",e)
  })


}
