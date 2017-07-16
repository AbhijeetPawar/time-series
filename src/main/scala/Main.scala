import java.time.Instant

import Analysis.PriceRatio

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val Array(filePath, window, _*) = args

    parser(filePath)
      .scanLeft(EmptyAnalyzer: Analyzer) { (analyzer, pr) => analyzer.create(window.toInt, pr) }
      .drop(1)
      .map(analyzer => analyzer.analyze())
      .foreach(println)
  }

  private def parser(filePath: String): Iterator[PriceRatio] = {
    Source.fromFile(filePath)
      .getLines()
      .map(line => line.split("\\s+"))
      .map { case Array(ts, ratio, _*) => PriceRatio(Instant.ofEpochSecond(ts.toInt), ratio.toDouble) }
  }
}
