package iot.pood.management.repository

import iot.pood.management.dao.Dao
import iot.pood.management.dao.connector.MongoImplicit
import iot.pood.management.dao.internal.MongoDao
import iot.pood.management.model.UserModel.{ConverterImplicit, LogRecord, Token, User}

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 23.10.2017.
  */
trait ManagementRepository extends RepositoryCreator{

  def userDao: Dao[User,String] = UserRepository

  def logDao: Dao[LogRecord,String] = LogRepository

  def tokenDao: Dao[Token,String] = TokenRepository

}

object ManagementRepository extends ManagementRepository

trait RepositoryCreator extends MongoImplicit with ConverterImplicit {

  import ExecutionContext.Implicits.global

  object UserRepository extends MongoDao[User,String]("users")

  object LogRepository extends MongoDao[LogRecord,String]("logs")

  object TokenRepository extends MongoDao[Token,String]("tokens")
}
