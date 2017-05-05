import java.io.File

/**
  * Created by marcin on 5/4/17.
  */


sealed abstract class Index(file: File){
  def this(path:String){
    this(new File(path))
  }
  val handler: File
  override def toString: String = s"Index of ${handler.getAbsolutePath}"
  def find(name: String): List[_ <: Index] = {

   List(this).filter(_.handler.getName==name)
  }

}

case class FileIndex(private val file: File) extends Index(file) {
  override val handler: File =  {
    if(!file.exists || file.isDirectory) throw NotAFileException(file)
    file
  }
}

object FileIndex{
  def apply(fileName: String): FileIndex = FileIndex(new File(fileName))
}

case class DirectoryIndex(private val directory: File) extends Index(directory) {

  override val handler: File = {
    if(!directory.exists || directory.isFile) throw NotADirectoryException(directory)
    directory
  }

  def subFiles: List[FileIndex] = handler.listFiles.filter(_.isFile).map(FileIndex(_)).toList
  def subDirectories: List[DirectoryIndex] = handler.listFiles.filter(_.isDirectory).map(DirectoryIndex(_)).toList

  override def find(fileName: String):List[_ <: Index] = {
    (subFiles ++ subDirectories).map(_.find(fileName)).fold(super.find(fileName))(_ ++ _)
  }
}

object DirectoryIndex{
  def apply(directoryName: String): DirectoryIndex = DirectoryIndex(new File(directoryName))
}



object Main extends App{


  val index = DirectoryIndex(".")
  println(index.handler.getAbsolutePath)
  println(index.subFiles)
  println(index.subDirectories)
  println(index)
  println("search for .")

  for (index <- index.find(".")) println(index.handler.getAbsolutePath)
  println("search for .idea")
  for (index <- index.find("Index.scala")) println(index.handler.getAbsolutePath)


}


