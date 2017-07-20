package analyzer

import java.time.Instant

import analyzer.Analysis.{PriceRatio, PriceRatioAnalysis}
import org.scalatest.{FunSpec, Matchers}

class AnalyzerTest extends FunSpec with Matchers {
  val window = 60

  describe("Analyzer") {
    it("should create analyzer given priceRatio sample") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      val analyzers = Analyzer.analyze(window)(Iterator(priceRatio))

      analyzers.toStream should contain theSameElementsAs List(
        Analyzer(List(), priceRatio)
      )
    }

    it("should create next analyzer given next priceRatio sample") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.900)

      val nextAnalyzer = Analyzer.analyze(window)(Iterator(priceRatio1, priceRatio2))

      nextAnalyzer.toStream should contain theSameElementsAs List(
        Analyzer(List(), priceRatio1),
        Analyzer(List(priceRatio1), priceRatio2))
    }

    it("should create next analyzer based on window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.9)
      val priceRatio3 = PriceRatio(Instant.ofEpochSecond(660), 1.9)

      val nextAnalyzer = Analyzer.analyze(window)(Iterator(priceRatio1, priceRatio2, priceRatio3))

      nextAnalyzer.toStream should contain theSameElementsAs List(
        Analyzer(List(), priceRatio1),
        Analyzer(List(priceRatio1), priceRatio2),
        Analyzer(List(priceRatio2), priceRatio3)
      )
    }
  }

  describe("Analysis") {

    it("should analyze new price ratio when no previous analysis present") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      val analyzer = Analyzer(List(), priceRatio)

      assert(analyzer.getAnalysis === PriceRatioAnalysis(priceRatio, 1, 1.805, 1.805, 1.805))
    }

    it("should analyze new price ratio when previous analysis present in same time window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.900)

      val analyzer = Analyzer(List(priceRatio1), priceRatio2)

      assert(analyzer.getAnalysis === PriceRatioAnalysis(priceRatio2, 2, 3.705, 1.805, 1.9))
    }

  }
}
