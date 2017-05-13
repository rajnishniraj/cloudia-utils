import akka.actor.{ActorSystem, Props}
import communication.Node

/**
  * Created by marcin on 5/13/17.
  */
object NodeTest {
  def main(args: Array[String]): Unit = {
    val system1 = ActorSystem("test1")
    val system2 = ActorSystem("test2")
    val host = "testhost"
    val port = 1234
    val chunksize= 100
    val node1 = system1.actorOf(Props(classOf[Node], chunksize, host, port), name = "node1")
    val node2 = system2.actorOf(Props(classOf[Node], chunksize, host, port), name = "node2")
    node1 ! system2.actorSelection(node2.path)
  }





}
