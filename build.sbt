import sbt._


lazy val util = (project in file("iot-pood-util"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.baseDependencies)


lazy val base = (project in file("iot-pood-base"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.baseDependencies)
    .dependsOn(util)


lazy val integration = (project in file("iot-pood-integration"))
  .settings(Common.settings: _*)
  .dependsOn(util,base)
  .settings(libraryDependencies ++=Dependencies.integrationDependencies)

lazy val mqtt = (project in file("iot-pood-mqtt"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++=Dependencies.iotMessagingDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base% "compile->compile;test->test")

lazy val rules = (project in file("iot-pood-rules"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.rulesEngineDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base% "compile->compile;test->test",integration)

lazy val service = (project in file("iot-pood-service"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.serviceDependencies)
    .enablePlugins(sbtdocker.DockerPlugin)
    .dependsOn(util,base% "compile->compile;test->test",integration)

lazy val engine = (project in file("iot-pood-engine"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.serviceDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base% "compile->compile;test->test",integration)

lazy val storage = (project in file("iot-pood-storage"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++=Dependencies.storageDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base% "compile->compile;test->test",integration)

lazy val management = (project in file("iot-pood-management"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++=Dependencies.managementDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base% "compile->compile;test->test",integration)


lazy val root = (project in file(".")).
  aggregate(base,util,engine,service,integration,mqtt,storage,management,rules)


