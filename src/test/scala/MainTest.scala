import org.scalatest.FlatSpec

class MainTest extends FlatSpec {

  "sum on List(1,2,3)" should "equal 6" in {
    val list = List(1,2,3)
    assert(list.sum === 6)
  }
}
