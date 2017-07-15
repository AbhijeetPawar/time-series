import java.time.Instant

import Analysis.PriceRatio
import org.scalatest.FunSpec

class AnalyzerTest extends FunSpec {

  describe("EmptyAnalyzer") {
    it("should create current analyzer given next priceRatio sample") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val analyzer = EmptyAnalyzer

      val nextAnalyzer = analyzer.create(priceRatio)

      assert(nextAnalyzer === CurrentAnalyzer(List(), priceRatio))
    }

    it("should throw UnsupportedOperationException on analyze") {
      assertThrows[UnsupportedOperationException](EmptyAnalyzer.analyze())
    }
  }

  describe("CurrentAnalyzer") {
    it("should create next analyzer given next priceRatio sample") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatioNext = PriceRatio(Instant.ofEpochSecond(601), 1.900)
      val analyzer = CurrentAnalyzer(List(), priceRatio)

      val nextAnalyzer = analyzer.create(priceRatioNext)

      assert(nextAnalyzer === CurrentAnalyzer(List(priceRatio), priceRatioNext))
    }

    it("should create next analyzer filtering previous ratios if they are outside window") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatioNext = PriceRatio(Instant.ofEpochSecond(661), 1.900)
      val analyzer = CurrentAnalyzer(List(), priceRatio)

      val nextAnalyzer = analyzer.create(priceRatioNext)

      assert(nextAnalyzer === CurrentAnalyzer(List(), priceRatioNext))
    }

    it("should analyze new price ratio when no previous analysis present") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      val analyzer = CurrentAnalyzer(List(), priceRatio)

      assert(analyzer.analyze() === Analysis.Analysis(priceRatio, 1, 1.805, 1.805, 1.805))
    }

    it("should analyze new price ratio when previous analysis present in same time window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.900)

      val analyzer = CurrentAnalyzer(List(priceRatio1), priceRatio2)

      assert(analyzer.analyze() === Analysis.Analysis(priceRatio2, 2, 3.705, 1.9, 1.805))
    }

    it("should analyze new price ratio for time window 60s") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.9)
      val priceRatio3 = PriceRatio(Instant.ofEpochSecond(660), 1.9)

      val analyzer = CurrentAnalyzer(List(priceRatio2, priceRatio1), priceRatio3)

      assert(analyzer.analyze() === Analysis.Analysis(priceRatio3, 2, 3.8, 1.9, 1.9))
    }
  }
}
