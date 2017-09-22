package iot.pood.storage

import java.util.Calendar

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, ActorSystem}
import com.datastax.driver.core.utils.UUIDs
import iot.pood.storage.actor.Storage
import iot.pood.storage.actor.Storage.{DataNotSaved, DataSaved, SaveDeviceDataRecord, SaveDeviceStateDateRecord}
import iot.pood.storage.service.internal.DeviceDataRecordService
import iot.pood.storage.service.internal.DeviceStateDataRecordService
import iot.pood.storage.entity.CassandraEntity._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn
import scala.util.{Failure, Success}
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import com.outworkers.phantom.ResultSet

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationDouble

/**
  * Created by rafik on 4.8.2017.
  */
object StorageApplication extends App{

  StdIn.readLine()
}

class SenderActor(storage: ActorRef) extends Actor  with ActorLogging{

  override def receive: Receive ={
    case result: DataSaved => {
      log.info("DataSaved: {}",result)
    }
    case result: DataNotSaved => {
      log.info("RESULT: {} DataNotSaved: {}",result)
    }
    case y @ _ => {
      log.info("Class: {}",y.getClass)
    }
  }


  @scala.throws[Exception](classOf[Exception])
  override def preStart() = {
    super.preStart()
    storage ! SaveDeviceStateDateRecord(1l,DeviceStateDataRecord("2",Calendar.getInstance().getTime(),"test"))
  }
}

