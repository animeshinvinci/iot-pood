package iot.pood.management.security

/**
  * Created by rafik on 11.10.2017.
  */
trait TokenService {

  import iot.pood.base.model.user.UserMessages._
  import iot.pood.base.model.security.SecurityMessages._

  def parse(token: String):User

  def create(uniqueUser: User): Token


}
