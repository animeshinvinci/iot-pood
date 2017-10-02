package iot.pood.base.json.exception

import akka.http.scaladsl.model.StatusCodes
import iot.pood.base.json.JsonApi
import spray.json._

/**
  * Created by rafik on 28.9.2017.
  */
trait ErrorMessages {

  this: JsonApi =>

  import iot.pood.base.json.base.BaseMessages._

  /**
    * Global error message
    *
    */
  trait ErrorMessage extends Message{
    def message: Option[String]
  }

  /**
    * Definition of Validation message
    *
    * @tparam A
    */
  trait ValidationMessage[A] extends ErrorMessage with DataMessage[A]


  /**
    * Field validation message
    *
    * @param field - field
    * @param message - message
    */
  case class ValidationFieldMessage(field: String, message: String)

  /**
    * Validation message used globally for validation client input. Return with StatusCodes.BadRequest
    *
    * @param code - HTTP code
    * @param appCode - application code. Can be different as code property
    * @param message - global message inform about general info regarding response
    * @param data - data contains ValidationFieldMessage
    */
  case class ValidationExceptionMessage(override val code: Int,override val appCode: Option[Int] = None,
                                        override val message: Option[String] = None,override val data: Set[ValidationFieldMessage] = Set())
    extends ValidationMessage[Set[ValidationFieldMessage]]
  {

    def this(data:Set[ValidationFieldMessage]) = this(StatusCodes.BadRequest.intValue,None,None,data)

    def this(appCode: Integer,message: String, data: Set[ValidationFieldMessage]) = this(StatusCodes.BadRequest.intValue,
                Some(appCode),Some(message),data)
  }


  /**
    * Internal server error message
    *
    * @param code
    * @param appCode
    * @param message
    */
  case class InternalServerErrorMessage(override val code: Int,override val appCode: Option[Int],
                                        override val message: Option[String]) extends ErrorMessage{

    def this() = this(StatusCodes.InternalServerError.intValue,None,Some(StatusCodes.InternalServerError.defaultMessage))

  }


  implicit val validationFieldFormat = jsonFormat2(ValidationFieldMessage)
  implicit val validationExceptionFormat = jsonFormat4(ValidationExceptionMessage)
  implicit val internalServerErrorExceptionFormat=  jsonFormat3(InternalServerErrorMessage)

}
