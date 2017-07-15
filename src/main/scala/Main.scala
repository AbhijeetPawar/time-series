import java.time.Instant

import Analysis.PriceRatio

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val filePath = args.head

    Source.fromFile(filePath)
      .getLines()
      .map(line => line.split("\\s+"))
      .map { case Array(ts, ratio, _*) => PriceRatio(Instant.ofEpochSecond(ts.toInt), ratio.toDouble) }
      .scanLeft(EmptyAnalyzer: Analyzer) { (analyzer, pr) => analyzer.create(pr) }
      .drop(1)
      .map(analyzer => analyzer.analyze())
      .foreach(println)
  }
}
