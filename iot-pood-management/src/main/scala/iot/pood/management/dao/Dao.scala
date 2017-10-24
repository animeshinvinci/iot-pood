package iot.pood.management.dao

import iot.pood.management.dao.Results.DaoResult
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

/**
  * Created by rafik on 17.10.2017.
  */
trait Dao[T,I] {

  def save(entity: T): Future[WriteResult]

//  def delete(id: I): Future[DaoResult]
//
//  def update(entity: T): Future[DaoResult]
//
//  def findById(id: I): Future[DaoResult]

}
