package communication

import index.FileIndex
import io.Chunkifier

/**
  * Created by marcin on 5/8/17.
  */


/** Message for requesting updated DirectoryIndex  **/
case class Ping() extends Serializable

/** Request for object represented by element
  *
  * @param element representation of requested object, e.g. FileIndex
  */
case class Request[T](element: T) extends Serializable

/** Confirmation sent from node to server informing it's ready to download file
  *
  * @param fileManifest - corresponding FileManifest
  */
case class Confirmation(fileManifest: FileManifest) extends Serializable

/** Manifest representing file prepared to be split into chunks.
  *
  * @param fileIndex FileIndex representation of file
  * @param chunkSize size of chunks that file is split into
  */
case class FileManifest(fileIndex: FileIndex, implicit val chunkSize: Int) extends Serializable {
  val chunkCount: Int = Chunkifier.chunksCount(fileIndex.handler, chunkSize)
}



