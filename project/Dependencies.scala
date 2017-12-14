import sbt._

object Dependencies {
  lazy val backendDepencencies =
    configDependencies ++
      catsDependencies ++
      documentationDependencies ++
      testDependencies ++
      loggingDependencies ++
      akkaDependencies ++
      akkaHttpDependencies ++
      monocleDependcencies ++
      persistanceDependencies


  lazy val functionalTestDependencies = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.scalaj" %% "scalaj-http" % "2.3.0",
    "io.spray" %%  "spray-json" % "1.3.3",
    "org.skinny-framework.com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.4",
    "com.sun.jersey" % "jersey-core" % "1.19",
    "com.sun.jersey" % "jersey-client" % "1.19",
    "com.sun.jersey.contribs" % "jersey-multipart" % "1.19",
    "org.jfarcand" % "jersey-ahc-client" % "1.0.5",
    "io.swagger" % "swagger-core" % "1.5.8",
    "joda-time" % "joda-time" % "2.2",
    "org.joda" % "joda-convert" % "1.2"
  )

  lazy val scalaTestVersion = "3.0.1"
  lazy val configDependencies = Seq(
    "com.typesafe" % "config" % "1.3.1"
  )

  lazy val catsDependencies = Seq(
    "org.typelevel" %% "cats" % "0.9.0"
  )
  lazy val documentationDependencies = Seq(
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0"
  )

  lazy val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "com.danielasfregola" %% "random-data-generator" % "2.0" % "test",
    "com.github.fakemongo" % "fongo" % "2.1.0" % "test",
    "org.mongodb" % "mongodb-driver" % "3.5.0" % "test"//for fongo
  )
  lazy val loggingDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.slf4j" % "slf4j-simple" % "1.7.25"
  )

  lazy val akkaVersion = "2.5.6"
  lazy val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.0"
  )

  lazy val akkaHttpVersion = "10.0.10"
  lazy val akkaHttpDependencies = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "ch.megard" %% "akka-http-cors" % "0.2.1"
  )

  lazy val monocleVersion = "1.4.0"
  lazy val monocleDependcencies = Seq(
    "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-law"   % monocleVersion % "test"
  )

  lazy val persistanceDependencies = Seq(
    "org.mongodb" % "mongodb-driver-async" % "3.5.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",
    "org.mongodb.scala" %% "mongo-scala-bson" % "2.1.0",
    "org.reactivemongo" %% "reactivemongo" % "0.12.7",
    "org.reactivemongo" %% "reactivemongo-akkastream" % "0.12.7",
    "com.github.scullxbones" %% "akka-persistence-mongo-rxmongo" % "2.0.4"
  )
}
