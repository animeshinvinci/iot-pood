package iot.pood.util

import java.io.ByteArrayOutputStream

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io._
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}


/**
  * Created by rafik on 15.11.2017.
  */
object Application extends App{

  val schemaString = "\n{\n    \"namespace\": \"kakfa-avro.test\",\n     \"type\": \"record\",\n     \"name\": \"user\",\n     \"fields\":[\n         {  \"name\": \"id\", \"type\": \"int\"},\n         {   \"name\": \"name\",  \"type\": \"string\"},\n         {   \"name\": \"email\", \"type\": [\"string\", \"null\"]}\n     ]\n}"

  val schema: Schema = new Schema.Parser().parse(schemaString)

  val genericRecord: GenericRecord = new GenericData.Record(schema)

  genericRecord.put("id",1)
  genericRecord.put("name", "singh")
  genericRecord.put("email", null)

  val writer = new SpecificDatumWriter[GenericRecord](schema)
  val out = new ByteArrayOutputStream()
  val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
  writer.write(genericRecord, encoder)
  encoder.flush()
  out.close()

  val serializedBytes: Array[Byte] = out.toByteArray()
  println(serializedBytes)


  val reader: DatumReader[GenericRecord] = new SpecificDatumReader[GenericRecord](schema)
  val decoder: BinaryDecoder = DecoderFactory.get().binaryDecoder(serializedBytes, null)
  val userData: GenericRecord = reader.read(null, decoder)



  println(userData.get("id"))
  println(userData.get("name"))
  println(userData.get("email"))
}
