import java.io.File

/**
  * Created by marcin on 5/5/17.
  */
class Manifesto(file: File)



case class FileManifesto(file: File) extends Manifesto(file)

case class DirectoryManifesto(directory: File) extends Manifesto(directory)

