package observatory

import model.Station
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ModelTest  extends FunSuite with Checkers {

  test("Station ab"){
    val s = Station("a",Some("b"),Some(+1.2), Some(-2.3))

    assert(s.composedId == "ab")
  }

  test("Station a"){
    val s = Station("a",None,Some(+1.2), Some(-2.3))

    assert(s.composedId == "a")
  }

  test("Station location 1.2;-2.3"){
    val s = Station("a",None,Some(+1.2), Some(-2.3))

    assert(s.location === Some(Location(1.2, -2.3)))
  }

  test("Station location none"){
    val s = Station("a",None,Some(+1.2), None)

    assert(s.location === None)
  }


}

