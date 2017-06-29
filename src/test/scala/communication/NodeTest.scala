package communication

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import index.{DirectoryIndex, FileIndex, Index}

import scala.concurrent.duration.{Duration, SECONDS}

/**
  * Created by mike on 6/29/17.
  */
class NodeTest() extends TestKit(ActorSystem("NodeTest")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val nodePath = "/home/mike/Programming/scala/Cloudia/"

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Node" must {
    "send back directory index if pinged" in {
      val node = system.actorOf(Node.props(nodePath))

      node ! Ping()
      expectMsgPF() {
        case DirectoryIndex(_) => ()
      }
    }
  }
}
