//import akka.actor.{ActorSystem, Props}
//import akka.pattern.{ask, pipe}
//import communication.{Ping, Node, Request}
//import index.DirectoryIndex
//import akka.util.Timeout
//
//import scala.concurrent.duration._
//import scala.concurrent.{Await, Future}
//
///**
//  * Created by marcin on 5/13/17.
//  */
//object NodeTest {
//  def main(args: Array[String]): Unit = {
//    val system1 = ActorSystem("test1")
//    val system2 = ActorSystem("test2")
//    val chunksize = 1000000
//    val node1 = system1.actorOf(Props(classOf[Node], chunksize, "/home/marcin/Documents/Coding/cloudia/test1"), name = "node1")
//    val node2 = system2.actorOf(Props(classOf[Node], chunksize, "/home/marcin/Documents/Coding/cloudia/test2"), name = "node2")
//    val index = Await.result(node2.ask(Ping())(1 second).mapTo[DirectoryIndex], 1 second)
//    index.subDirectories.foreach(println(_))
//    index.subFiles.foreach(println(_))
//    node2.tell(Request(index.subDirectories.head.subFiles.head), node1)
//
//
//  }
//
//
//}
