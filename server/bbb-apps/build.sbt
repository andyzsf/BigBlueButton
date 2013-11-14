
organization  := "bbb-apps"

version       := "0.1"

scalaVersion  := "2.10.2"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.6",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/"
)

// We want to have our jar files in lib_managed dir.
// This way we'll have the right path when we import
// into eclipse.
retrieveManaged := true

testOptions in Test += Tests.Argument("html", "console", "junitxml")

libraryDependencies ++= 
  Seq(
	  "io.spray"            %   "spray-can"       % "1.2-RC3",
	  "io.spray"            %   "spray-routing"   % "1.2-RC3",
	  "io.spray"            %   "spray-testkit"   % "1.2-RC3" % "test",
	  "io.spray"            %%  "spray-json"      % "1.2.5",
	  "com.typesafe.akka"   %%  "akka-actor"      % "2.3-M1",
	  "com.typesafe.akka"   %%  "akka-camel"      % "2.3-M1",
	  "com.typesafe.akka"   %%  "akka-testkit"    % "2.3-M1" % "test",
	  "org.specs2"          %%  "specs2"          % "1.14" % "test",
	  "org.json4s"          %%  "json4s-native"   % "3.2.4",
	  "com.typesafe.akka" 	%%  "akka-slf4j"      % "2.3-M1",
	  "ch.qos.logback"    	%   "logback-classic"   % "1.0.3",
	  "org.pegdown" 		% 	"pegdown" 			% "1.4.0",
	  "junit" 				% 	"junit" 			% "4.11"
	)


seq(Revolver.settings: _*)

