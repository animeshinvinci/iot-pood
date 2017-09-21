package iot.pood.service.model

/**
  * Created by rafik on 26.6.2017.
  */
object RequestModel {

  trait DataRecord{
    def data: String
  }

  trait MapDataRecord{
    def  data: Map[String,String]
  }


  case class SimpleDataRecord(data: String) extends DataRecord

  case class SimpleMapDataRecord(data: Map[String,String]) extends MapDataRecord

}
