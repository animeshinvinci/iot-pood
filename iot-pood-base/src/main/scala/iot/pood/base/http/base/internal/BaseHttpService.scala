package iot.pood.base.http.base.internal

import akka.actor.ActorRef
import akka.util.Timeout
import iot.pood.base.config.HttpConfig.HttpConfig
import iot.pood.base.http.base.{ApiVersionService, HttpApiService}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, DurationDouble}

/**
  * Created by rafik on 3.10.2017.
  */
abstract class BaseHttpService(implicit executorContext: ExecutionContext) extends ApiVersionService {

  implicit val timeout = Timeout(1 seconds)

}