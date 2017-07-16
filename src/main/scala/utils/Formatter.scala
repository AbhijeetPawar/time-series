package utils

import java.text.DecimalFormat

object Formatter {

  def formatRow(t: String, v: String, n: String, rs: String, minV: String, maxV: String): String = {
    leftPad(12, t) + leftPad(10, v) + leftPad(5, n) + leftPad(10, rs) + leftPad(10, minV) + maxV
  }

  def leftPad(length: Int, str: String): String = {
    ("%-" + length + "s").format(str)
  }

  def formatDouble(x: Double): String = {
    lazy val formatter = new DecimalFormat("0.#####")

    formatter.format(x)
  }
}
