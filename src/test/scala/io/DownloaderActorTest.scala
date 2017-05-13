package io

import java.io.File
import java.util.concurrent.TimeUnit.SECONDS

import akka.actor.{ActorSystem, Props}
import communication.{FileManifesto, Node}
import org.scalatest.{BeforeAndAfterEach, FunSuite}

import scala.concurrent.duration.FiniteDuration



/**
  * Created by marcin on 5/9/17.
  */
class DownloaderActorTest extends FunSuite with BeforeAndAfterEach {
  val system = ActorSystem("test")
  override def beforeEach() {


  }

  override def afterEach() {


//    system.terminate()

  }

  test("sendTest"){
    implicit val chunkSize = 10240
    val file = new File("build.sbt")
    val manifesto = FileManifesto(file, chunkSize, "testfiles/build.sbt")
    implicit val timeout: FiniteDuration = FiniteDuration(1, SECONDS)

    val downloader = system.actorOf(Props(classOf[DownloaderActor], manifesto, timeout))
    val uploader = system.actorOf(Props(classOf[UploaderActor], downloader, manifesto))


  }
}
