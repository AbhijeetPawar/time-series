import Analysis.{Analysis, PriceRatio}

sealed trait Analyzer {
  def create(windowLength: Int, next: PriceRatio): Analyzer

  def analyze(): Analysis
}

case object EmptyAnalyzer extends Analyzer {
  override def create(windowLength: Int, next: PriceRatio): Analyzer = CurrentAnalyzer(Stream.empty, next)

  override def analyze(): Analysis = throw new UnsupportedOperationException("Cannot Analyse EmptyAnalyzer")
}

case class CurrentAnalyzer(previousWindow: Stream[PriceRatio], priceRatio: PriceRatio) extends Analyzer {
  override def create(windowLength: Int, next: PriceRatio): Analyzer = {
    def nextWindow = (priceRatio #:: previousWindow).filter(pr => windowed(windowLength, next, pr))

    CurrentAnalyzer(nextWindow, next)
  }

  override def analyze(): Analysis = {
    previousWindow
      .map(r => r.analysis())
      .foldLeft(priceRatio.analysis()) { (acc, next) =>
        Analysis.Analysis(acc.priceRatio, acc.n + 1, acc.rs + next.rs, Math.min(acc.minV, next.minV), Math.max(acc.maxV, next.maxV))
      }
  }

  private def windowed(windowLength: Int, next: PriceRatio, current: PriceRatio): Boolean = {
    next.timestamp.minusSeconds(windowLength).isBefore(current.timestamp)
  }
}