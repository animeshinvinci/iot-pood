package iot.pood.management.model

import java.util.Date

import ch.qos.logback.classic.joran.action.ConfigurationAction
import reactivemongo.bson.Macros.Annotations.Key
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONHandler, BSONObjectID, Macros}

/**
  * Created by rafik on 20.10.2017.
  */
object UserModel {


  case class User(@Key("_id") id: String, login: String, email: String, password: String, status:String, created: Date,configs: Map[String,ConfigParameter])

  case class ConfigParameter(key: String, value: String)

  case class Token(@Key("_id") id: String,token: String, login: String,created: Date,expired: Date)

  case class LogRecord(@Key("_id") id: String,created: Date,action: String,loginUser: String, message: String){
    def this(created: Date,action: String,loginUser: String, message: String) = this(null,created,action,loginUser,message)
  }


  trait UserConverter {
    this: ConfigParameterConverter =>

    implicit def userWriter: BSONDocumentWriter[User] = Macros.writer[User]

    implicit def userReader: BSONDocumentReader[User] = Macros.reader[User]
  }

  trait ConfigParameterConverter {

    implicit def configParameterWriter:BSONDocumentWriter[ConfigParameter] = Macros.writer[ConfigParameter]

    implicit def configParameterReader:BSONDocumentReader[ConfigParameter] = Macros.reader[ConfigParameter]

  }

  trait TokenConverter {
    implicit def tokenWriter:BSONDocumentWriter[Token] = Macros.writer[Token]
    implicit def tokenReader:BSONDocumentReader[Token] = Macros.reader[Token]
  }

  trait LogConverter {
    implicit def logWriter:BSONDocumentWriter[LogRecord] = Macros.writer[LogRecord]
    implicit def logReader:BSONDocumentReader[LogRecord] = Macros.reader[LogRecord]
  }


  trait ConverterImplicit extends UserConverter
    with ConfigParameterConverter
    with TokenConverter
    with LogConverter

}
