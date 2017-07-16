import java.time.Instant

import Analysis.PriceRatio

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val Array(filePath, window, _*) = args

    val executor = parser _ andThen analyzer andThen evaluator(window.toInt)

    executor(filePath)
  }

  private def parser(filePath: String): Iterator[PriceRatio] = {
    Source.fromFile(filePath)
      .getLines()
      .map(line => line.split("\\s+"))
      .map { case Array(ts, ratio, _*) => PriceRatio(Instant.ofEpochSecond(ts.toInt), ratio.toDouble) }
  }

  private def analyzer(ratios: Iterator[PriceRatio]): Iterator[Analyzer] = {
    ratios
      .scanLeft(Option.empty: Option[Analyzer]) { (analyzer, pr) =>
        analyzer match {
          case None => Option(Analyzer.create(pr))
          case Some(_analyzer) => Option(Analyzer.create(pr, _analyzer))
        }
      }
      .flatten
  }

  private def evaluator(window: Int)(analyzers: Iterator[Analyzer]): Unit = {
    analyzers
      .map(analyzer => analyzer.analyze(window))
      .foreach(println)
  }

}
