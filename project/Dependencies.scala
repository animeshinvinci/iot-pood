import sbt._
import Keys._

object Dependencies {

  lazy val Versions = new {

    val safeConfig = "1.2.1"

    val akka = "2.4.17"
    val akkaHttp = "10.0.5"
    val phantom = "2.13.3"
    val util = "0.30.1"

    val logback = "1.2.3"

    val scalaTest = "3.0.2"
    val kafkaClient = "0.10.2.1"
    val sprayJson = "1.3.3"
    val mqttVersion = "21d5ff44+20161107-1333"
    val jodaTime = "2.16.0"

    val jwt       = "0.4.5"

    val mongo     = "0.12.7"
    val enumerator = "1.5.12"

    val validator = "0.7.1"

    val avro = "1.7.7"
  }


  val logback   = "ch.qos.logback" % "logback-classic" % Versions.logback

  val enumerator =  "com.beachape" %% "enumeratum" % Versions.enumerator

  val avro = "org.apache.avro"  %  "avro"  %  Versions.avro


  val serialisation           :Seq[ModuleID] = Seq (
    avro
  )

  val commonDependencies      :Seq[ModuleID] = Seq(
    "org.scalatest"   %%  "scalatest"   %   Versions.scalaTest   % "test",
    logback,
    enumerator,
    "org.scala-lang" % "scala-reflect" % "2.12.0",
    "com.github.nscala-time" %% "nscala-time" % Versions.jodaTime
  )

  val validator               :Seq[ModuleID] = Seq(
    "com.wix" %% "accord-core" % Versions.validator,
    "org.scala-lang" % "scala-compiler" % "2.12.0" % "provided"
  )


  val jsonDependencies        :Seq[ModuleID] = Seq(
    "io.spray"         %%  "spray-json" %   Versions.sprayJson
  )

  val securityException       :Seq[ModuleID] = Seq{
    "com.jason-goodwin" %% "authentikat-jwt" % Versions.jwt
  }

  // akka dependencies
  val akkaDependencies        :Seq[ModuleID] = Seq(
    "com.typesafe.akka"   %% "akka-actor"             % Versions.akka,
    "com.typesafe.akka"   %% "akka-remote"            % Versions.akka,
    "com.typesafe.akka"   %% "akka-slf4j"             % Versions.akka,
    "com.typesafe.akka"   %% "akka-stream"            % Versions.akka,
    "com.typesafe.akka"   %% "akka-testkit"           % Versions.akka        % "test"
  )
  // akka http, REST
  val akkaHttpDependencies    :Seq[ModuleID] = Seq(
    "com.typesafe.akka"   %% "akka-http-spray-json"   % Versions.akkaHttp,
    "com.typesafe.akka"   %% "akka-http"              % Versions.akkaHttp,
    "com.typesafe.akka"   %% "akka-http-testkit"      % Versions.akkaHttp    % "test"
  )
  // cassandra, phantom
  val phantomDependencies     :Seq[ModuleID] = Seq(
    "com.outworkers"      %% "phantom-dsl"             % Versions.phantom exclude("io.netty", "netty-handler"),
    "com.outworkers"      %% "util-testing"           % Versions.util        % "test"
  )

  // mqtt, akka integration
  val mqttDependencies         :Seq[ModuleID] = Seq(
//    "com.typesafe.akka"   %% "akka-stream-alpakka-mqtt" % Versions.mqttVersion
  )

  val kafkaDependencies       :Seq[ModuleID] = Seq(
    "net.cakesolutions"   %% "scala-kafka-client-akka"    % Versions.kafkaClient,
    "net.cakesolutions"   %% "scala-kafka-client-testkit" % Versions.kafkaClient % "test"
  )

  val testDependencies        :Seq[ModuleID] = Seq(
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

  val mongoDependencies       :Seq[ModuleID] = Seq(
    "org.reactivemongo" %% "reactivemongo" % Versions.mongo,
    "com.beachape" %% "enumeratum-reactivemongo-bson" % Versions.enumerator
  )

  val playDependencies        :Seq[ModuleID] = Seq(
    "com.google.inject" % "guice" % "4.1.0",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )

  val webDependencies           :Seq[ModuleID] = playDependencies

  val utilDependencies          :Seq[ModuleID] = commonDependencies ++ jsonDependencies ++ serialisation
  val baseDependencies          :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ jsonDependencies ++ akkaHttpDependencies ++ testDependencies ++ serialisation
  val serviceDependencies       :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ akkaHttpDependencies
  val engineDependencies        :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ akkaHttpDependencies
  val storageDependencies       :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ phantomDependencies
  val integrationDependencies   :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ kafkaDependencies ++ utilDependencies
  val iotMessagingDependencies  :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ mqttDependencies ++ kafkaDependencies
  val rulesEngineDependencies   :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ kafkaDependencies
  val managementDependencies    :Seq[ModuleID] =  commonDependencies ++ akkaDependencies ++ akkaHttpDependencies ++ securityException ++ mongoDependencies ++ validator
}