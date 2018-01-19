package iot.pood.base.json.api

import iot.pood.base.json.base.DateJson
import iot.pood.base.json.enums.EnumJson
import iot.pood.base.json.support.circle.CircleJson

trait JsonApiV1 extends CircleJson
  with EnumJson
  with DateJson
{

}
