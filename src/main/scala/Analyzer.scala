import Analysis.{Analysis, PriceRatio}

case class Analyzer (previousWindow: Stream[PriceRatio], priceRatio: PriceRatio) {
  def analyze(windowLength: Int): Analysis = {
    previousWindow
      .filter(pr => windowed(windowLength, priceRatio, pr))
      .map(r => r.analysis())
      .foldLeft(priceRatio.analysis()) { (acc, next) =>
        Analysis.Analysis(acc.priceRatio, acc.n + 1, acc.rs + next.rs, Math.min(acc.minV, next.minV), Math.max(acc.maxV, next.maxV))
      }
  }

  private def create(next: PriceRatio) = {
    Analyzer(priceRatio #:: previousWindow, next)
  }

  private def windowed(windowLength: Int, next: PriceRatio, current: PriceRatio): Boolean = {
    next.timestamp.minusSeconds(windowLength).isBefore(current.timestamp)
  }
}

object Analyzer {
  def create(priceRatio: PriceRatio): Analyzer = {
    Analyzer(Stream(), priceRatio)
  }

  def create(priceRatio: PriceRatio, analyzer: Analyzer): Analyzer = {
    analyzer.create(priceRatio)
  }
}