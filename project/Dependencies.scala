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
  }


  val logback   = "ch.qos.logback" % "logback-classic" % Versions.logback

  val commonDependencies      :Seq[ModuleID] = Seq(
    "org.scalatest"   %%  "scalatest"   %   Versions.scalaTest   % "test",
//    "com.typesafe"    %% "config"       %   Versions.safeConfig,
    logback,
    "org.scala-lang" % "scala-reflect" % "2.12.0",
    "com.github.nscala-time" %% "nscala-time" % Versions.jodaTime
  )

  val jsonDependencies        :Seq[ModuleID] = Seq(
    "io.spray"         %%  "spray-json" %   Versions.sprayJson
  )

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


  val utilDependencies          :Seq[ModuleID] = commonDependencies ++ jsonDependencies
  val baseDependencies          :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ jsonDependencies  ++ akkaHttpDependencies
  val serviceDependencies       :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ akkaHttpDependencies
  val engineDependencies        :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ akkaHttpDependencies
  val storageDependencies       :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ phantomDependencies
  val integrationDependencies   :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ kafkaDependencies ++ utilDependencies
  val iotMessagingDependencies  :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ mqttDependencies ++ kafkaDependencies
  val rulesEngineDependencies   :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ kafkaDependencies
  val managementDependencies    :Seq[ModuleID] = commonDependencies ++ akkaDependencies ++ akkaHttpDependencies
}