package iot.pood.base.json.base

import io.circe.{Decoder, Encoder, HCursor}
import iot.pood.base.json.support.circle.CircleJson
import org.joda.time.DateTime

trait DateJson extends CircleJson{
  import org.joda.time.format.DateTimeFormat
  import org.joda.time.format.DateTimeFormatter

  val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")

  val changeDateFormat:DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-yyyy-HH-mm")

  implicit val timeEncored: Encoder[DateTime] = new Encoder[DateTime]{
    override def apply(a: DateTime) = Encoder.encodeString.apply(a.toString(dateFormat))
  }

  implicit val timeDecoder: Decoder[DateTime] = new Decoder[DateTime] {
    override def apply(c: HCursor): Decoder.Result[DateTime] = Decoder.decodeString.map(s => dateFormat.parseDateTime(s)).apply(c)
  }
}
