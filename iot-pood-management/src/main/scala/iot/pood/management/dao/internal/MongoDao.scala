package iot.pood.management.dao.internal

import iot.pood.management.dao.Dao
import iot.pood.management.dao.connector.MongoDb
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by rafik on 18.10.2017.
//  */
class MongoDao[T,I](name: String)(implicit writer: BSONDocumentWriter[T],reader: BSONDocumentReader[T],mongo:MongoDb
                                              ,ex: ExecutionContext) extends Dao[T,I]{
  require(name != null,"Name of collection must be defined")
  require(mongo != null,"Mongo object can't be null")

  val collection: Future[BSONCollection] = mongo.createCollection(name)


  override def save(entity: T): Future[WriteResult] = collection.flatMap(_.insert(entity))
}
