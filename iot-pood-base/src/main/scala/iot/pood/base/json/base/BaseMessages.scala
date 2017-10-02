package iot.pood.base.json.base

/**
  * Created by rafik on 28.9.2017.
  */
object BaseMessages {

  trait Message {
    def code: Int
    def appCode: Option[Int]
  }

  trait DataMessage[A] {
    def data: A
  }

  trait CompleteDataMessage[A] extends Message with DataMessage[A]

}
