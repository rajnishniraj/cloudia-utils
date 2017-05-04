import java.io.File

import scala.collection.immutable.HashSet




/**
  * Created by marcin on 5/4/17.
  */

case class NotADirectoryException(file: File) extends Exception

sealed class DirectoryIndex(directory: File){
  def this(path:String){
    this(new File(path))
  }

  val mainDirectory: File = {
    if(!directory.exists || directory.isFile)
      throw NotADirectoryException(directory)
    else directory
  }

  def subFiles: Set[File] = mainDirectory.listFiles.filter(_.isFile).toSet
  def subDirectories: Set[File] = mainDirectory.listFiles.filter(_.isDirectory).toSet
  def subIndices: Set[DirectoryIndex] = subDirectories.map(new DirectoryIndex(_))

  override def toString: String = s"Index of ${mainDirectory.getAbsolutePath}"

  def find(fileName: String): Set[File] = {
    Set(mainDirectory).filter(_.getName==fileName) union
      subFiles.filter(_.getName==fileName) union
      subIndices
        .map(_.find(fileName))
        .fold(new HashSet[File]){(acc, s) => acc ++ s}
  }
}

object DirectoryIndex extends App{

  val index = new DirectoryIndex(".")
  println(index.mainDirectory.getAbsolutePath)
  println(index.subFiles)
  println(index.subDirectories)
  println(index)
  println("search for .")

  for (file <- index.find(".")) println(file.getAbsolutePath)
  println("search for .idea")
  for (file <- index.find("DirectoryIndex.scala")) println(file.getAbsolutePath)


}


