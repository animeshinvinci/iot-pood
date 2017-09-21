package iot.pood.service.json
import iot.pood.service.model.ResponseModel._
/**
  * Created by rafik on 26.6.2017.
  */
trait JsonDataResponse {

  this: SprayJson =>
  implicit val simpleResponseFormat = jsonFormat2(SimpleResponse)

}
