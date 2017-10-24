package iot.pood.management.dao

/**
  * Created by rafik on 17.10.2017.
  */
object Results {

  sealed trait DaoResult {
    def result: Boolean
  }

  case class SuccessDaoResult(override val result: Boolean) extends DaoResult

  case class ErrorDaoResult(override val result: Boolean,error:Option[String] = None) extends DaoResult

}
