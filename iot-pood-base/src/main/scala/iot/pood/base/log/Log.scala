package iot.pood.base.log

import org.slf4j.LoggerFactory

/**
  * Logger. Not used in Actor. Used only in thread safe application
  *
  * Created by rafik on 21.9.2017.
  */
trait Log {

  lazy val log  = LoggerFactory.getLogger(this.getClass)

}
