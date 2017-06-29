package communication

import java.io.File

import index.FileIndex
import io.Chunkifier

/**
  * Created by marcin on 5/8/17.
  */

case class Ping() extends Serializable

case class Request[T](element: T) extends Serializable

case class Confirmation(fileManifest: FileManifest) extends Serializable

case class FileManifest(fileIndex: FileIndex, implicit val chunkSize: Int) extends Serializable {
  val chunkCount: Int = Chunkifier.chunksCount(fileIndex.handler, chunkSize)
}



