package iot.pood.base.http.service.internal

import akka.actor.ActorRef
import iot.pood.base.config.HttpConfig.HttpConfig
import iot.pood.base.http.service.{ApiVersionService, HttpApiService}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

/**
  * Created by rafik on 3.10.2017.
  */
abstract class BaseHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends ApiVersionService