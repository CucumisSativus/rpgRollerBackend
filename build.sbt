name := "rpgRoller"

version := "1.0"

scalaVersion := "2.12.1"

val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"
val monocleVersion = "1.4.0"
resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

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

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.0",
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test",
  "org.typelevel" %% "cats" % "0.9.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.danielasfregola" %% "random-data-generator" % "2.0",
  "com.typesafe" % "config" % "1.3.1",
  "ch.megard" %% "akka-http-cors" % "0.2.1",
  "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0",
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)
        