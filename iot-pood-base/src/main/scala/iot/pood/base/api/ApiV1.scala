package iot.pood.base.api

import iot.pood.base.http.base.HttpEndPointVersion

/**
  * Created by rafik on 4.10.2017.
  */
trait ApiV1 extends HttpEndPointVersion{

  val VERSION = "v1"
  override def version: String = VERSION
}
