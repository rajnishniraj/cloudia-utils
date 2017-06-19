# cloudia-utils
Core logic of Cloudia nodes, communication and indices. Written with (ab)use of [akka library](http://akka.io/). Applications using this libary are: [cloudia-server](https://github.com/mprzewie/cloudia-server) and [cloudia-client](https://github.com/mprzewie/cloudia-client/blob/master/README.md).

## adding as a dependency
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


## documentation 
Coming soon.

This is a part of a project for Scala Programming class by mgr. Łukasz Faber and dr. Roman Dębski at AGH UST.
