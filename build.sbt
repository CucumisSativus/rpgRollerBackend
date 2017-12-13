import Dependencies._
import sbt.Keys.resolvers

name := "rpgRoller"

version := "1.0"

scalaVersion := "2.12.4"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
enablePlugins(DockerPlugin, JavaAppPackaging)
scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
)

javaOptions ++= Seq(
  "--add-modules java.xml.bind"
)

javaOptions in Universal ++= Seq(
  "-Dconfig.file=/roller-conf/application.conf",
  "--add-modules java.xml.bind",
  "-Dlogback.configurationFile=/roller-conf/logback.xml"
)

libraryDependencies ++= backendDepencencies




val functional = (project in file("functionalTest"))
    .settings(
      libraryDependencies ++= functionalTestDependencies,
      resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    )
