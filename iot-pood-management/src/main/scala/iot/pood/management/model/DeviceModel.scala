package iot.pood.management.model

import iot.pood.management.model.BaseModel.{DataType, DateTimeConverter, ObjectStatus}
import org.joda.time.DateTime
import reactivemongo.bson.{BSONDateTime, BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONReader, BSONWriter, Macros}
import reactivemongo.bson.Macros.Annotations.Key

/**
  * Created by rafik on 25.10.2017.
  */
object DeviceModel {

  trait BaseObject {
    def id: Option[String]
    def created: DateTime
    def version: String
  }

  case class DashBoard(@Key("_id")id: Option[String],created: DateTime,
                       version: String,
                       status: ObjectStatus,info: String, location: String,config: Map[String,Config]) extends BaseObject

  case class Config(id: String,created: DateTime,
                    key: String, value: String)

  case class DeviceGroup(@Key("_id")id: Option[String],created: DateTime,
                         version: String,name: String,comment: String
                         ,status: ObjectStatus,config: Map[String,Config])
        extends BaseObject


  case class Device(@Key("_id")id: Option[String],created: DateTime,
                    version: String,name: String,comment: String
                    ,status: ObjectStatus,config: Map[String,Config],
                    dataHolder:Map[String,DataHolder]) extends BaseObject

  case class DataHolder(id: String,dataType: DataType,info: String)


  trait DeviceModelConverter extends DateTimeConverter{

    implicit def dashBoardWriter: BSONDocumentWriter[DashBoard] = Macros.writer[DashBoard]
    implicit def dashBoardReader:BSONDocumentReader[DashBoard] = Macros.reader[DashBoard]


    implicit def deviceGroupWriter: BSONDocumentWriter[DeviceGroup] = Macros.writer[DeviceGroup]
    implicit def deviceGroupReader:BSONDocumentReader[DeviceGroup] = Macros.reader[DeviceGroup]

    implicit def deviceWriter: BSONDocumentWriter[Device] = Macros.writer[Device]
    implicit def deviceReader:BSONDocumentReader[Device] = Macros.reader[Device]

    implicit def dataHolderWriter: BSONDocumentWriter[DataHolder] = Macros.writer[DataHolder]
    implicit def dataHolderReader:BSONDocumentReader[DataHolder] = Macros.reader[DataHolder]

    implicit def configWriter: BSONDocumentWriter[Config] = Macros.writer[Config]
    implicit def configReader:BSONDocumentReader[Config] = Macros.reader[Config]

  }
}
