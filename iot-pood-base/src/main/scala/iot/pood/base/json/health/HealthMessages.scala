package iot.pood.base.json.health

import akka.http.scaladsl.model.StatusCodes
import iot.pood.base.json.JsonApi
import iot.pood.base.json.base.BaseMessages.CompleteDataMessage

/**
  * Messages used for health API
  *
  * Created by rafik on 28.9.2017.
  */
trait HealthMessages  {

  this: JsonApi =>

  trait Parameter{
    def key: String
    def value: Any
  }

  case class AppParameter(override val key: String,override val value: String) extends Parameter


  /**
    * Health response data
    *
    * @param code
    * @param appCode
    * @param message
    * @param data
    */
  case class HealthResponse(code: Int, appCode: Option[Int] = None,message: Option[String] = None, data: Map[String,AppParameter])
    extends CompleteDataMessage[Map[String,AppParameter]]{
    def this(data: Map[String,AppParameter]) = this(StatusCodes.OK.intValue,None,None,data)
    def this(appCode: Integer,message: String,data: Map[String,AppParameter]) = this(StatusCodes.OK.intValue,Some(appCode),Some(message),data)
  }

  /**
    * Marshaling HealthMessages
    *
    */

  implicit val appIntegerParameterFormat = jsonFormat2(AppParameter)
  implicit val healthResponseFormat = jsonFormat4(HealthResponse)

}
