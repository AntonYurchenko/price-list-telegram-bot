package ruby.sources

import java.io.File

import scala.io.Source

object PriceSource {

  def readActual(dir: File, partition: Int = 100): Seq[Seq[String]] = {
    val fileList = dir.listFiles()
    if (fileList.isEmpty) Nil
    else {
      val lastTsFile = fileList.max
      val price = Source.fromFile(lastTsFile, "UTF-8").getLines().toSeq
      if (price.length <= 100) price :: Nil
      else price.grouped(100).toSeq
    }
  }

}
