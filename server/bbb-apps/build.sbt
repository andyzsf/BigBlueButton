
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

libraryDependencies ++= {
  val akkaVersion  = "2.2.3"
  val sprayVersion = "1.2-RC3"
  Seq(
	  "io.spray"            %   "spray-can"       % sprayVersion,
	  "io.spray"            %   "spray-routing"   % sprayVersion,
	  "io.spray"            %   "spray-testkit"   % sprayVersion % "test",
	  "io.spray"            %%  "spray-json"      % "1.2.5",
	  "com.typesafe.akka"   %%  "akka-camel"      % akkaVersion,
	  "com.typesafe.akka"   %%  "akka-actor"      % akkaVersion,
	  "com.typesafe.akka"   %%  "akka-testkit"    % akkaVersion % "test",
	  "org.specs2"          %%  "specs2"          % "2.2.3" % "test",
	  "org.scalatest"       %   "scalatest_2.10"  % "2.0" % "test",
	  "com.typesafe.akka" 	%%  "akka-slf4j"      % akkaVersion,
	  "ch.qos.logback"    	%   "logback-classic" % "1.0.3",
	  "org.pegdown" 		    % 	"pegdown" 			  % "1.4.0",
	  "junit" 				      %   "junit"           % "4.11",
	  "com.etaty.rediscala" %% "rediscala"        % "1.3",
	  "commons-codec"       %   "commons-codec"   % "1.8"
	)}


seq(Revolver.settings: _*)

