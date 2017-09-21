package iot.pood.service.model

/**
  * Created by rafik on 26.6.2017.
  */
object ResponseModel {

  trait ApiResponse{
    def status: String
    def code: String
  }

  case class SimpleResponse(status: String, code: String) extends ApiResponse



}
