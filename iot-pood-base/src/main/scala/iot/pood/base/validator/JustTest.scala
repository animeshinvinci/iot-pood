package iot.pood.base.validator

/**
  * Created by rafik on 6.11.2017.
  */
object JustTest extends App{

  case class Context(s: Boolean)


  implicit val context = Context(s = true)

  class JustX(implicit context: Context) {

    def justName(name: String)(implicit context: Context): Unit =
    {
      println(s" name: $name context: ${context.s}")
    }

  }


  object xxx {




  }

}
