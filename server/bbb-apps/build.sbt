
organization  := "bbb-apps"

version       := "0.1"

scalaVersion  := "2.10.2"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "rediscala" at "https://github.com/etaty/rediscala-mvn/raw/master/releases/"
)


// We want to have our jar files in lib_managed dir.
// This way we'll have the right path when we import
// into eclipse.
retrieveManaged := true

testOptions in Test += Tests.Argument("html", "console", "junitxml")

libraryDependencies ++= 
  Seq(
	  "io.spray"            %   "spray-can"       % "1.2-M8",
	  "io.spray"            %   "spray-routing"   % "1.2-M8",
	  "io.spray"            %   "spray-testkit"   % "1.2-M8" % "test",
	  "io.spray"            %%  "spray-json"      % "1.2.5",
	  "com.typesafe.akka"   %%  "akka-camel"      % "2.2.0-RC1",
	  "com.typesafe.akka"   %%  "akka-actor"      % "2.2.0-RC1",
	  "com.typesafe.akka"   %%  "akka-testkit"    % "2.2.0-RC1" % "test",
	  "org.specs2"          %%  "specs2"          % "1.14" % "test",
	  "org.json4s"          %%  "json4s-native"   % "3.2.4",
	  "org.mongodb"         %%  "casbah"          % "2.6.2",
	  "com.typesafe.akka" 	%%  "akka-slf4j"      % "2.2.0-RC1",
	  "ch.qos.logback"    	%   "logback-classic"   % "1.0.3",
	  "org.pegdown" 		% 	"pegdown" 			% "1.4.0",
	  "junit" 				% 	"junit" 			% "4.11",
	  "com.etaty.rediscala" %% "rediscala"        % "1.3"
	)


seq(Revolver.settings: _*)

