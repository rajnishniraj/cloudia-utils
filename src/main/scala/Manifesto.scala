import java.io.File
import math._

/**
  * Created by marcin on 5/5/17.
  */
trait Manifesto extends Serializable{
  def name: String
}

case class FileManifesto(private val file: File, implicit val chunkSize: Long) extends Manifesto{
  def chunksCount: Int = ceil(file.length.toDouble/chunkSize).toInt
  override def name: String = file.getName


}



