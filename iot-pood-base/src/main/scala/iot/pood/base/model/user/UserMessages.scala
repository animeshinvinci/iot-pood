package iot.pood.base.model.user

/**
  * Created by rafik on 11.10.2017.
  */
object UserMessages {

  trait User {
    def id: String
  }


  case class SimpleUser(id: String) extends User

}
