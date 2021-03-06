ThisBuild / scalaVersion := ScalaVersions.v213
//ThisBuild / crossScalaVersions := Seq(ScalaVersions.v213, ScalaVersions.v3M3, ScalaVersions.v3RC1)
ThisBuild / crossScalaVersions := Seq(ScalaVersions.v213)
//ThisBuild / crossScalaVersions := Seq(ScalaVersions.v213, ScalaVersions.v3M3)

lazy val basicSettings = Seq(
  scalacOptions ~= (_.filterNot(
    Set(
      "-Wdead-code",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:imports",
      "-Wunused:params"
    )
  )),
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) =>
      Seq(
        "-Ymacro-annotations"
      )
    case Some((3, _)) => Seq()
    case _            => Seq()
  }),
  scalacOptions in (Compile, doc) ~= (_.filterNot(
    Set(
      "-scalajs",
      "-deprecation",
      "-explain-types",
      "-explain",
      "-feature",
      "-language:existentials,experimental.macros,higherKinds,implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Ykind-projector",
      "-from-tasty",
      "-encoding",
      "utf8",
    )
  ))
)

lazy val commonSettings = basicSettings ++ Seq(
  requireJsDomEnv in Test := true,
  parallelExecution in Test := false,
  scalaJSUseMainModuleInitializer := true,
  scalaJSLinkerConfig in (Compile, fastLinkJS) ~= {
    _.withSourceMap(false)
  },
  Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  testFrameworks += new TestFramework("munit.Framework")
)

lazy val bundlerSettings = Seq(
  jsEnv := new net.exoego.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
  installJsdom / version := BuildSettings.version.jsdom,
  useYarn := true
)

lazy val baseDependencies = Seq(
  libraryDependencies ++= Seq(
    ("com.raquo" %%% "laminar" % BuildSettings.version.laminar).withDottyCompat(scalaVersion.value),
//    ("org.scalatest" %%% "scalatest"    % BuildSettings.version.`scala-test` % Test).withDottyCompat(scalaVersion.value),
    ("app.tulz"     %%% "stringdiff"   % BuildSettings.version.stringdiff   % Test).withDottyCompat(scalaVersion.value),
    ("com.raquo"    %%% "domtestutils" % BuildSettings.version.domtestutils % Test).withDottyCompat(scalaVersion.value),
    "org.scalameta" %%% "munit"        % BuildSettings.version.munit        % Test
  )
)

lazy val catsDependencies = Seq(
  libraryDependencies ++= Seq(
    ("org.typelevel" %%% "cats-core" % BuildSettings.version.cats).withDottyCompat(scalaVersion.value)
  )
)

lazy val `core` =
  project
    .in(file("modules/core"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar extensions"
    )

lazy val `cats` =
  project
    .in(file("modules/cats"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(catsDependencies)
    .settings(
      libraryDependencies ++= Seq(
        "org.typelevel"              %%% "cats-laws"                 % "2.3.1" % Test,
        "org.typelevel"              %%% "discipline-munit"          % "1.0.5" % Test,
        "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % "1.2.5" % Test
      )
    )
    .settings(noPublish) // nothing here yet
    .settings(
      description := "Laminar utilities (cats)"
    ).dependsOn(`core`)

lazy val `validation-core` =
  project
    .in(file("modules/validation-core"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar utilities (validation)"
    ).dependsOn(`core`)

lazy val `validation-cats` =
  project
    .in(file("modules/validation-cats"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(catsDependencies)
    .settings(
      description := "Laminar utilities (validation+cats)"
    ).dependsOn(`validation-core`)

lazy val `fsm` =
  project
    .in(file("modules/fsm"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar utilities (validation)"
    )

lazy val `markdown` =
  project
    .in(file("modules/markdown"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar utilities (markdown)"
    )

lazy val `videojs` =
  project
    .in(file("modules/videojs"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar + video.js"
    ).dependsOn(`core`)

lazy val `highlight` =
  project
    .in(file("modules/highlight"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Laminar + highlight.js"
    )

lazy val `ui` =
  project
    .in(file("modules/ui"))
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(bundlerSettings)
    .settings(
      description := "Laminar UI"
    ).dependsOn(`core`)

lazy val `tailwind` =
  project
    .in(file("modules/tailwind"))
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(bundlerSettings)
    .settings(
      description := "Laminar UI (tailwindcss)"
    ).dependsOn(`core`, `ui`, `validation-core`)

lazy val `tailwind-default-theme` =
  project
    .in(file("modules/tailwind-default-theme"))
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .settings(commonSettings)
    .settings(
      description := "Laminar UI (tailwindcss default theme)"
    ).dependsOn(`tailwind`)

lazy val `websocket` =
  project
    .in(file("modules/websocket"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Websocket client"
    )

lazy val `websocket-circe` =
  project
    .in(file("modules/websocket-circe"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core"   % BuildSettings.version.circe,
        "io.circe" %%% "circe-parser" % BuildSettings.version.circe
      )
    )
    .settings(
      description := "circe support for websocket client"
    )
    .dependsOn(`websocket`)

lazy val `fetch` =
  project
    .in(file("modules/fetch"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Fetch client"
    )

lazy val `fetch-circe` =
  project
    .in(file("modules/fetch-circe"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core"   % BuildSettings.version.circe,
        "io.circe" %%% "circe-parser" % BuildSettings.version.circe
      )
    )
    .settings(
      description := "circe support for the fetch client"
    )
    .dependsOn(`fetch`)

lazy val `util` =
  project
    .in(file("modules/util"))
    .enablePlugins(ScalaJSPlugin)
    .settings(commonSettings)
    .settings(baseDependencies)
    .settings(
      description := "Misc utilities"
    )

lazy val website = project
  .in(file("website"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(EmbeddedFilesPlugin)
  .settings(basicSettings)
  .settings(noPublish)
  .settings(
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(false)) },
    scalaJSUseMainModuleInitializer := true,
//    scalaJSLinkerConfig ~= (_.withModuleSplitStyle(org.scalajs.linker.interface.ModuleSplitStyle.FewestModules)),
    libraryDependencies ++= Seq(
      "com.raquo"     %%% "laminar"              % BuildSettings.version.laminar,
      "io.frontroute" %%% "frontroute"           % BuildSettings.version.frontroute,
      "com.yurique"   %%% "embedded-files-macro" % BuildSettings.version.`embedded-files`,
      "com.lihaoyi"   %%% "sourcecode"           % BuildSettings.version.sourcecode,
      "io.circe"      %%% "circe-generic"        % BuildSettings.version.circe
    ),
    embedTextGlobs := Seq("**/*.md"),
    embedDirectories ++= (Compile / unmanagedSourceDirectories).value,
    (Compile / sourceGenerators) += embedFiles
  )
  .dependsOn(
    `core`,
    `cats`,
    `validation-cats`,
    `util`,
    `fsm`,
    `videojs`,
    `highlight`,
    `markdown`,
    `ui`,
    `tailwind`,
    `tailwind-default-theme`,
    `websocket`,
    `websocket-circe`,
    `fetch`,
    `fetch-circe`
  )

lazy val noPublish = Seq(
  publishLocal / skip := true,
  publish / skip := true,
  publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))
)

lazy val root = project
  .in(file("."))
  .settings(noPublish)
  .settings(
    name := "laminext"
  )
  .aggregate(
    `core`,
    `cats`,
    `validation-core`,
    `validation-cats`,
    `util`,
    `fsm`,
    `videojs`,
    `highlight`,
    `markdown`,
    `ui`,
    `tailwind`,
    `tailwind-default-theme`,
    `fetch`,
    `fetch-circe`,
    `websocket`,
    `websocket-circe`
  )
