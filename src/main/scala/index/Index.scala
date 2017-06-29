/**
  * Created by marcin on 5/4/17.
  */
package index

import java.io.File
import java.nio.file.{Files, Paths}

import util.{NotADirectoryException, NotAFileException}

/** Abstraction over File for unifying Files and Directories
  *
  * @param file handler
  */
sealed abstract class Index(file: File) extends Serializable {
  def this(path: String) {
    this(new File(path))
  }

  val handler: File
  val path: String

  override def toString: String = s"index.Index of $path"

  def find(name: String): List[_ <: Index] = {
    List(this).filter(_.handler.getName == name)
  }

}

/** Represents regular files.
  *
  * @param file        handler
  * @param homeDirPath path to home directory in file system containing this file
  */
case class FileIndex(private val file: File)(private implicit val homeDirPath: String) extends Index(file) {
  override val handler: File = {
    if (!file.exists || file.isDirectory) throw NotAFileException(file)
    file
  }
  override val path: String = Paths.get(homeDirPath).relativize(Paths.get(handler.getCanonicalPath)).toString
  val writable: Boolean = Files.isWritable(handler.toPath)
  val readable: Boolean = Files.isReadable(handler.toPath)
  val executable: Boolean = Files.isExecutable(handler.toPath)
}

object FileIndex {
  def apply(fileName: String)(implicit homeDirPath: String): FileIndex = FileIndex(new File(fileName))(homeDirPath)
}

/** Represents directory files.
  *
  * @param directory   file handler
  * @param homeDirPath path to home directory in file system containing this directory
  */
case class DirectoryIndex(private val directory: File)(private implicit val homeDirPath: String) extends Index(directory) {
  override val handler: File = {
    if (!directory.exists || directory.isFile) throw NotADirectoryException(directory)
    directory
  }
  override val path: String = Paths.get(homeDirPath).relativize(Paths.get(handler.getCanonicalPath)).toString
  val subFiles: List[FileIndex] = handler.listFiles.filter(_.isFile).map(FileIndex(_)(homeDirPath)).toList
  val subDirectories: List[DirectoryIndex] = handler.listFiles.filter(_.isDirectory).map(DirectoryIndex(_)(homeDirPath)).toList

  override def find(fileName: String): List[_ <: Index] = {
    (subFiles ++ subDirectories).map(_.find(fileName)).fold(super.find(fileName))(_ ++ _)
  }
}

object DirectoryIndex {
  def apply(directoryName: String)(implicit homeDirPath: String): DirectoryIndex = {
    DirectoryIndex(new File(directoryName))(homeDirPath)
  }
}






