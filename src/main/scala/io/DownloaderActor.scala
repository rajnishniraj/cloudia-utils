package io

/**
  * Created by marcin on 5/7/17.
  */

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor._
import communication._

import scala.collection.mutable.ListBuffer

class DownloaderActor(manifesto: FileManifesto) extends Actor {
  val chunksBuffer: ListBuffer[Chunk] = ListBuffer()


  override def receive: PartialFunction[Any, Unit] = {

//    case confirmation: Confirmation =>
//      println(s"Sending confirmation to ${remoteWaiter.path}")
//      remoteWaiter ! confirmation

    case chunk: Chunk =>
      println(s"received chunk ${chunk.id}")
      if(!chunksBuffer.exists(_.id == chunk.id)) chunksBuffer += chunk
      if(chunksBuffer.length==manifesto.chunkCount){
        try{
          build()
        } catch {
          case e: Exception => println("Building Error!")
        }
      }
    case msg =>
      println(s"Received $msg")


  }

  def build():Unit ={
    println("Builder started")
    val chunks = chunksBuffer.toList
    println("Chunks listified")
    var name = manifesto.name

    while (new File(name).exists()){
      name+="(1)"
    }
    println(s"Trying to build $name")
    val newFile = new File(name)
    val writer = new BufferedWriter(new FileWriter(newFile))
    for(i <- 0 until manifesto.chunkCount){
      writer.write(chunks.filter(c => c.id == i).head.content.utf8String)
    }
    writer.close()


  }
}
