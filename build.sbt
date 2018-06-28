name := "ScalaTest"

version := "0.1"

scalaVersion := "2.12.6"
val akkaVersion = "2.4.14"
val akkaHttpVersion = "10.1.3"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",

  "com.github.dnvriend" %% "akka-persistence-jdbc" % "3.4.0",
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.85",
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.85" % Test
)

