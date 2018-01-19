package iot.pood.management.http.device

import javax.ws.rs.Path

import akka.actor.{ActorRef, ActorSystem}
import iot.pood.base.api.ApiV1
import iot.pood.base.http.base.internal.BaseHttpService
import iot.pood.management.actors.device.DeviceActor
import iot.pood.management.dto.DeviceJson
import iot.pood.management.http.group.GroupHttpService.log
import iot.pood.management.dto.DeviceDto.{CreateDevice, CreateDeviceWithGroup, UpdateDevice}
import akka.pattern.ask
import akka.util.Timeout
import io.swagger.annotations._

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.Flow
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}

object DeviceHttpService {

  def apply()(implicit executionContext: ExecutionContext,actorSystem: ActorSystem)  = {

    log.info("Create DEVICE service")
    val deviceActor = actorSystem.actorOf(DeviceActor.props)
    new DeviceHttpService(deviceActor)
  }

}

@Api(value = "/add", produces = "application/json")
@Path("/add")
class DeviceHttpService(serviceWorker: ActorRef)(implicit executorContext: ExecutionContext) extends BaseHttpService
  with ApiV1 with DeviceJson{
  import DeviceActor._

  override def route = pathPrefix("devices"){
    join(createWithGroup,create,del)
  }

  @ApiOperation(value = "Find a ping", notes = "Returns a pong", httpMethod = "GET", response = classOf[String])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "Authorization", required = true,
      dataType = "string", paramType = "header")))
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Pong not found"),
    new ApiResponse(code = 200, message = "Pong found"),
    new ApiResponse(code = 400, message = "Invalid Ping supplied")))
  val createWithGroup = post {
    pathPrefix("withGroup") {
      entity(as[CreateDeviceWithGroup]) { deviceWithGroup => {
        log.info("Create Device with group: {}", deviceWithGroup)
        complete("ok")
        }
      }
    }
  }


  val create = post {
    entity(as[CreateDevice]){ device => {
        log.info("Create Device: {}",device)
        complete {
          (serviceWorker ? Create(device)).mapTo[String]
        }
      }
    }
  }

//  val update = put {
//    entity(as[UpdateDevice]){ device => {
//        log.info("Update device: {}",device)
//        complete("ok")
//      }
//    }
//  }

  val del = delete {
    pathPrefix(Segment) { id =>
      pathEndOrSingleSlash {
        log.info("Delete device ID: {}",id)
        complete("ok")
      }
    }
  }

}

