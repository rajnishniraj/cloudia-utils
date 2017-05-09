package communication

import java.io.File

import scala.math.ceil

/**
  * Created by marcin on 5/8/17.
  */
case class Request(filename: String) extends Serializable
case class Confirmation(system: String, host: String, port: Int, downloaderName: String) extends Serializable

abstract class Manifesto extends Serializable{
  def name: String
}

case class FileManifesto(private val file: File, implicit val chunkSize: Long) extends Manifesto {
  def chunksCount: Int = ceil(file.length.toDouble / chunkSize).toInt

  override def name: String = file.getName

}