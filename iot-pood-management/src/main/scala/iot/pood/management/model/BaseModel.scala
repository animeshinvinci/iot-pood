package iot.pood.management.model

import reactivemongo.bson.Macros.Annotations.Key
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONObjectID}

/**
  * Created by rafik on 20.10.2017.
  */
object BaseModel {

  case class BaseEntity(@Key("_id") id: Option[String] )


  trait ModelConverter[T]{

    implicit def writer:BSONDocumentWriter[T]

    implicit def reader:BSONDocumentReader[T]

  }

  trait ConverterPart[T] {
    this: ModelConverter[T] =>

    implicit lazy val partWriter = writer
    implicit lazy val partReader = reader
  }
}
