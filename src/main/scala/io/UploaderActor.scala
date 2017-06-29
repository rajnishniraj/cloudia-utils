package io

import akka.actor._
import communication.{FileManifest, Request}

/**
  * Created by marcin on 5/7/17.
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

object UploaderActor {
  def props(downloaderActor: ActorRef, fileManifesto: FileManifest)
           (implicit homeDirPath: String): Props = {
    Props(new UploaderActor(downloaderActor, fileManifesto))
  }
}
