import java.time.Instant
import java.text.DecimalFormat

object Analysis {
  type Ratio = Double

  case class PriceRatio(timestamp: Instant, ratio: Ratio) {
    def analysis(): Analysis = {
      Analysis(this, 1, ratio, ratio, ratio)
    }
  }

  case class Analysis(priceRatio: PriceRatio, n: Int, rs: Ratio, minV: Ratio, maxV: Ratio) {
    val format = new DecimalFormat("0.#####")

    private def formatDouble(x: Double): String = format.format(x)

    override def toString: String = Array(
      priceRatio.timestamp.getEpochSecond,
      formatDouble(priceRatio.ratio),
      n, formatDouble(rs), formatDouble(minV), formatDouble(maxV)
    )
      .mkString(" ")
  }
}
