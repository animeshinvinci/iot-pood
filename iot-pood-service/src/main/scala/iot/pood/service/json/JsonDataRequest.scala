package iot.pood.service.json


/**
  * Trait for json request marshaling
  *
  * Created by rafik on 26.6.2017.
  */
trait JsonDataRequest {

  this: SprayJson =>

  import iot.pood.service.model.RequestModel._
  implicit val dataRecordFormat = jsonFormat1(SimpleMapDataRecord)
  implicit val mapDataRecordFormat=  jsonFormat1(SimpleMapDataRecord)

}
