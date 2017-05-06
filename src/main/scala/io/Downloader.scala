package io

/**
  * Created by marcin on 5/7/17.
  */

import akka.actor._
class Downloader extends Actor {
  override def receive = {
    case msg: String =>
      println(s"received $msg")
  }
}
