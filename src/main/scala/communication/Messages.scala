package communication

import java.io.File

import io.Chunkifier

import scala.math.ceil

/**
  * Created by marcin on 5/8/17.
  */
case class Request(filename: String) extends Serializable
case class Confirmation(system: String, host: String, port: Int, downloaderName: String) extends Serializable



case class FileManifesto(private val file: File, implicit val chunkSize: Int) extends Serializable{
  def chunkCount: Int = Chunkifier.chunksCount(file, chunkSize)
  def name: String = file.getName

}