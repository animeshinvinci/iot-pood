package iot.pood.base.json.base

import java.text.SimpleDateFormat
import java.util.Date

import spray.json.{JsString, JsValue, JsonFormat}

import scala.util.Try

/**
  * Created by rafik on 24.8.2017.
  */
object DateMarshalling {
  implicit object DateFormat extends JsonFormat[Date] {
    def write(date: Date) = JsString(dateToIsoString(date))
    def read(json: JsValue) = json match {
      case JsString(rawDate) =>
        parseIsoDateString(rawDate)
          .fold(spray.json.deserializationError(s"Expected ISO Date format, got $rawDate"))(identity)
      case error => spray.json.deserializationError(s"Expected JsString, got $error")
    }
  }

  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  }

  private def dateToIsoString(date: Date) =
    localIsoDateFormatter.get().format(date)

  private def parseIsoDateString(date: String): Option[Date] =
    Try{ localIsoDateFormatter.get().parse(date) }.toOption
}