package io

import akka.actor._
import communication.FileManifesto

/**
  * Created by marcin on 5/7/17.
  */

class UploaderActor(downloaderActor: ActorRef, fileManifesto: FileManifesto) extends Actor{

  override def preStart(): Unit = {
   Chunkifier(fileManifesto.chunkSize, fileManifesto.file).foreach{
     chunk => downloaderActor ! chunk
   }
  }


  override def receive: PartialFunction[Any, Unit] = {
    case list: List[Chunk] =>
      println(s"Sending ${list.length} chunks")
      list.foreach{ elem =>
        downloaderActor ! elem}

  }
}
