package iot.pood.management.dao.connector

import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by rafik on 18.10.2017.
  */
trait MongoDb {

  def createCollection(name: String): Future[BSONCollection]

}

class MongoDatabase extends MongoDb {
  //need refactoring
  import ExecutionContext.Implicits.global
  private lazy val driver = MongoDriver()
  private lazy val parsedUri = MongoConnection.parseURI("mongodb://localhost:27017")
  private lazy val connection = parsedUri.map(driver.connection(_))
  private lazy val mongoConnection = for {
    mongo <- Future.fromTry(connection)
  } yield mongo

  private lazy val db = mongoConnection.flatMap(_.database("pood"))

  override def createCollection(name: String): Future[BSONCollection] = db.map(_.collection(name))

}
object MongoDatabase extends MongoDatabase


trait MongoImplicit {
  implicit lazy val mongo = MongoDatabase
}

