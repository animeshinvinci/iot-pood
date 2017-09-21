package iot.pood.base.utils

import org.joda.time.DateTime


/**
  * Created by rafik on 22.8.2017.
  */
trait RequestUniqueCreator {

  def createRequestId = DateTime.now().getMillis

  def dateTimeNow = DateTime.now()

  def dateNow = DateTime.now().toDate
}
