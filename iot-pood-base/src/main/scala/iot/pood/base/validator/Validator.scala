package iot.pood.base.validator

/**
  * Created by rafik on 6.11.2017.
  */
object Validator {


  trait Rules {

  }

  trait Result

  trait Validator[A] {
    def validate(v: A): Result
  }

}
