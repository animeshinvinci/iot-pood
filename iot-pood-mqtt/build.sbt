import sbt._

mainClass in assembly := Some("iot.pood.mqtt.MqttApplication")
assemblyJarName in assembly := s"mqtt-application-${version.value}.jar"

publishArtifact in(Compile, packageDoc) := false

publishTo := Some(Resolver.file("file", new File("artifacts")))

cleanFiles <+= baseDirectory { base => base / "artifacts" }


dockerfile in docker := {
  val baseDir = baseDirectory.value
  val artifact: File = assembly.value

  val imageAppBaseDir = "/app"
  val artifactTargetPath = s"$imageAppBaseDir/${artifact.name}"
  val artifactTargetPath_ln = s"$imageAppBaseDir/${name.value}.jar"


  //Define the resources which includes the entrypoint script
  val dockerResourcesDir = baseDir / "docker-resources"
  val dockerResourcesTargetPath = s"$imageAppBaseDir/"

  val appConfTarget = s"$imageAppBaseDir/conf/application" //boot-configuration.conf goes here
  val logConfTarget = s"$imageAppBaseDir/conf/logging" //logback.xml

  new Dockerfile {
    from("openjdk:8-jre")
    maintainer("rafaj.peter@gmail.com")
    expose(84, 8084)
    env("APP_BASE", s"$imageAppBaseDir")
    env("APP_CONF", s"$appConfTarget")
    env("LOG_CONF", s"$logConfTarget")
    copy(artifact, artifactTargetPath)
    copy(dockerResourcesDir, dockerResourcesTargetPath)
    copy(baseDir / "src" / "main" / "resources" / "logback.xml", logConfTarget) //Copy the default logback.xml
    //Symlink the service jar to a non version specific name
    run("ln", "-sf", s"$artifactTargetPath", s"$artifactTargetPath_ln")
    entryPoint(s"${dockerResourcesTargetPath}docker-entrypoint.sh")
  }
}
buildOptions in docker := BuildOptions(cache = false)

imageNames in docker := Seq(
  ImageName(
    //namespace = Some(organization.value),
    repository = name.value,
    // We parse the IMAGE_TAG env var which allows us to override the tag at build time
    tag = Some(sys.props.getOrElse("IMAGE_TAG", default = version.value))
  )
)

//---- end docker ----


