package iot.pood.storage.service

import com.outworkers.phantom.ResultSet

import scala.concurrent.Future

/**
  * Created by rafik on 9.8.2017.
  */
trait DataService[A] {

  def saveItem(record: A): Future[ResultSet]

  def saveItems(records: Set[A]): Future[Set[ResultSet]]

}
