package io

import akka.actor._

/**
  * Created by marcin on 5/7/17.
  */

class UploaderActor(downloaderActor: ActorRef) extends Actor{


  override def receive: PartialFunction[Any, Unit] = {
    case list: List[Any] =>
      list.foreach{ elem =>
        println(downloaderActor.path)
        downloaderActor ! elem}

  }
}
