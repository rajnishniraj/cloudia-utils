package io

import akka.actor._
import communication.{FileManifest, Request}

/**
  * Created by marcin on 5/7/17.
  */
/** Akka's actor for uploading chunked file.
  *
  * @param downloaderActor reference to DownloaderActor responsible for receiving file
  * @param fileManifest    manifest of file's chunks
  * @param homeDirPath     path to home directory of given file in filesystem
  */
private class UploaderActor(downloaderActor: ActorRef, fileManifest: FileManifest)(implicit homeDirPath: String) extends Actor {
  override def preStart(): Unit = {
    Chunkifier(fileManifest.chunkSize, fileManifest.fileIndex.handler).foreach {
      chunk =>
        downloaderActor ! chunk
    }
    downloaderActor ! ReceiveTimeout
  }

  override def receive: PartialFunction[Any, Unit] = {
    case Request(missingChunkId: Int) =>
      sender ! Chunkifier(fileManifest.chunkSize, fileManifest.fileIndex.handler)(missingChunkId)
  }
}

/** Companion object for creating Props representation of UploaderActor **/
object UploaderActor {
  def props(downloaderActor: ActorRef, fileManifesto: FileManifest)
           (implicit homeDirPath: String): Props = {
    Props(new UploaderActor(downloaderActor, fileManifesto))
  }
}
