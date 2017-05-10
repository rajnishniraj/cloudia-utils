package communication

import java.io.File

import io.Chunkifier

import scala.math.ceil

/**
  * Created by marcin on 5/8/17.
  */
case class Request(filename: String) extends Serializable

case class Confirmation(fileManifesto: FileManifesto) extends Serializable

case class FileManifesto(file: File, implicit val chunkSize: Int, newName: String = "") extends Serializable {
  val chunkCount: Int = Chunkifier.chunksCount(file, chunkSize)
  val name: String = newName match {
    case "" => file.getName
    case _ => newName
  }


}

