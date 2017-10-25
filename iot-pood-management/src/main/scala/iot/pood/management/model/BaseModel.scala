package iot.pood.management.model

import enumeratum._
import org.joda.time.DateTime
import reactivemongo.bson.{BSONDateTime, BSONReader, BSONWriter}

/**
  * Created by rafik on 20.10.2017.
  */
object BaseModel {

  sealed trait ObjectStatus extends EnumEntry

  object ObjectStatus extends Enum[ObjectStatus] with ReactiveMongoBsonEnum[ObjectStatus] {

    val values = findValues

    case object active   extends ObjectStatus
    case object testing  extends ObjectStatus
    case object disabled extends ObjectStatus
    case object stopped  extends ObjectStatus

  }


  sealed trait DataType extends EnumEntry

  object DataType extends Enum[DataType]  with ReactiveMongoBsonEnum[DataType] {

    val values = findValues

    case object switch   extends DataType
    case object parameter extends DataType
    case object analog      extends DataType

  }

  trait DateTimeConverter {

    implicit def dateTimeWriter: BSONWriter[DateTime,BSONDateTime] = BSONDateTimeWriter
    implicit def dateTimeReader:BSONReader[BSONDateTime,DateTime] = BSONDateTimeReader


    implicit object BSONDateTimeWriter extends BSONWriter[DateTime,BSONDateTime] {

      override def write(t: DateTime): BSONDateTime =  BSONDateTime(t.getMillis)
    }

    implicit object BSONDateTimeReader extends BSONReader[BSONDateTime,DateTime] {

      override def read(bson: BSONDateTime): DateTime = new DateTime(bson.value)
    }

  }

}
