package iot.pood.integration.json.integration

import java.util
import java.util.Calendar

import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages._
import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer, StringSerializer}
import spray.json._


/**
  * Created by rafik on 23.8.2017.
  */
object JsonIntegrationMessages extends DefaultJsonProtocol with NullOptions {

  import DateMarshalling._

  implicit val dataDeviceMessageFormat = jsonFormat2(DataDeviceMessage)
  implicit val dataDeviceStateMessageFormat = jsonFormat1(DataDeviceStateMessage)
  implicit val dataMessage = jsonFormat5(DataMessage)

  implicit val commandMessage = jsonFormat5(CommandMessage)

}

class SimpleStringSerializer[T](implicit val jsonWriter: JsonWriter[T]) extends Serializer[T]
{
  private val stringSerializer = new StringSerializer

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit ={
    stringSerializer.configure(configs,isKey)
  }

  override def serialize(topic: String, data: T) = {
    stringSerializer.serialize(topic,data.toJson.compactPrint)
  }

  override def close() = stringSerializer.close()
}

class SimpleStringDeserializer[T](implicit val jsonReader: JsonReader[T]) extends Deserializer[T]
{

  private val stringDeserializer = new StringDeserializer

  override def configure(configs: util.Map[String, _], isKey: Boolean) = stringDeserializer.configure(configs, isKey)

  override def close() = stringDeserializer.close()

  override def deserialize(topic: String, data: Array[Byte]) =  {
    stringDeserializer.deserialize(topic,data).parseJson.convertTo[T]
  }
}

