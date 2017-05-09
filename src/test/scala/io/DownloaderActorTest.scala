package io

import java.io.File

import akka.actor.{ActorSystem, Props}
import communication.FileManifesto
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Created by marcin on 5/9/17.
  */
class DownloaderActorTest extends FunSuite with BeforeAndAfterEach {
  val system = ActorSystem("test")
  override def beforeEach() {


  }

  override def afterEach() {
    system.terminate()

  }

  test("sendTest"){
    implicit val chunkSize = 10240
    val file = new File("testfiles/kierwa")
    val manifesto = FileManifesto(file, chunkSize, "testfiles/korwo")
    val downloader = system.actorOf(Props(classOf[DownloaderActor], manifesto))
    val uploader = system.actorOf(Props(classOf[UploaderActor], downloader))
    uploader ! Chunkifier(chunkSize, file)
  }
}