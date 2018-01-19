package iot.pood.base.json.enums

import iot.pood.base.json.support.circle.CircleJson

object EnumJson{

  import enumeratum.{CirceEnum, Enum, EnumEntry}

  sealed trait ResponseStatus extends EnumEntry

  case object ResponseStatus extends Enum[ResponseStatus] with CirceEnum[ResponseStatus]
  {
    case object ok extends ResponseStatus
    case object validation extends ResponseStatus
    case object error extends ResponseStatus

    val values = findValues
  }
}

trait EnumJson extends CircleJson{

}

