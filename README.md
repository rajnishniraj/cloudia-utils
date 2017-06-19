# cloudia-utils
Core logic of Cloudia nodes, communication and indices. Module shared by cloudia-server and cloudia-client.

This library should be used in projects which make use of Cloudia nodes. 
In order for sbt to include and fetch it from github as a dependency, it's recommended to add a following file inside the yourProject/project/ directory:

```scala

import sbt._

object MyBuild extends Build {
  lazy val root = Project("root", sbt.file(".")).dependsOn(cloudiaUtils)

  lazy val cloudiaUtils = ProjectRef(uri("git://github.com/mprzewie/cloudia-utils.git#master"), "cloudia-utils")
}

```

Unfortunately sbt doesn't update github dependencies automatically, so in order to catch up with updates in this library, you have to delete it from your project and sbt must fetch it again.
