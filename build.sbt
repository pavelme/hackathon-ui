name := "wix-hackathon-ui"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.ning" % "async-http-client" % "1.9.31"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
