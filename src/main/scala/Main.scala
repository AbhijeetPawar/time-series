import java.time.Instant

import Analysis.PriceRatio

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val Array(filePath, window, _*) = args

    val execute = parse _ andThen Analyzer.analyze andThen evaluate(window.toInt)

    execute(filePath)
  }

  private def parse(filePath: String): Iterator[PriceRatio] = {
    Source.fromFile(filePath)
      .getLines()
      .map(line => line.split("\\s+"))
      .map { case Array(ts, ratio, _*) => PriceRatio(Instant.ofEpochSecond(ts.toInt), ratio.toDouble) }
  }

  private def evaluate(window: Int)(analyzers: Iterator[Analyzer]): Unit = {
    analyzers
      .map(analyzer => analyzer.getAnalysis(window))
      .foreach(println)
  }

}
