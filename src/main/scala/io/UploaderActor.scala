package io

import akka.actor._

/**
  * Created by marcin on 5/7/17.
  */

class UploaderActor(downloaderActor: ActorRef) extends Actor{


  override def receive: PartialFunction[Any, Unit] = {
    case list: List[Chunk] =>
      println(s"Sending ${list.length} chunks")
      list.foreach{ elem =>
        downloaderActor ! elem}

  }
}
