name := "Cqrs_Experiment"

version := "0.1"

scalaVersion := "2.12.6"
val akkaVersion = "2.5.13"
val akkaHttpVersion = "10.1.3"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.4.0",
  "org.postgresql" % "postgresql" % "9.4.1208"
)

