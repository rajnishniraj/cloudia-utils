package communication

import java.io.File

import akka.actor._
import io.{DownloaderActor, UploaderActor}

import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit.SECONDS

import index.{DirectoryIndex, FileIndex}


/**
  * Created by marcin on 5/8/17.
  */
private class Node(chunkSize: Int, implicit val homeDirPath: String) extends Actor {
  implicit val timeout: FiniteDuration = FiniteDuration(1, SECONDS)

  def directoryIndex() = DirectoryIndex(homeDirPath)

  override def receive: Receive = {

    case Ping() =>
      sender ! directoryIndex()

    case Request(fileIndex: FileIndex) =>
      println(s"Request for ${fileIndex.path}")
      val path = homeDirPath + "/" + fileIndex.path
      sender ! FileManifest(fileIndex, chunkSize)


    case fileManifest: FileManifest =>
      val downloader = context.system.actorOf(DownloaderActor.props(fileManifest, timeout))
      println(downloader.path)
      val confirmation = Confirmation(fileManifest)
      sender.tell(confirmation, downloader)

    case Confirmation(fileManifesto) =>
      val uploader = context.system.actorOf(
        UploaderActor.props(sender, fileManifesto))
  }
}

object Node {
  val defaultSize = 1048576

  def props(homeDirPath: String)(implicit chunkSize: Int = defaultSize) = Props(new Node(chunkSize, homeDirPath))
}