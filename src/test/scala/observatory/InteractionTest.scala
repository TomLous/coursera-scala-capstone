package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  test("tileLocation") {
    assert(Interaction.tileLocation(0, 0, 0) === Location(85.05112877980659, -180.0))
    assert(Interaction.tileLocation(10, 10, 10) === Location(84.7383871209534, -176.484375))
    assert(Interaction.tileLocation(5, 100, 100) === Location(-89.99999212633796, 945.0))
  }

}
