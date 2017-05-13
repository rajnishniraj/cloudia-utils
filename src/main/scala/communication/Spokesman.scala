package communication

import akka.actor.Actor

/**
  * Created by marcin on 5/12/17.
  */
class Spokesman extends Actor{
  override def receive: PartialFunction[Any, Unit] = {
    case message: String => println(s"spokesman: $message")
  }
}
