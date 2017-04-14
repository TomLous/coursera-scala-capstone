package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {

  test("compare grid methods") {
    val x = 1
    val y = 1
    val zoom = 1
    val imageWidth = 5
    val imageHeight = 5


    val method1 = Visualization2.tileLocations(x, y, zoom, imageWidth, imageHeight).sortBy(_._1)
    //    println(Visualization2.tileLocations(0,0,0,5,5))


    val method2 = (0 until imageWidth * imageHeight).map(
      pos => {
        val relativeXPosInTile = (pos % imageWidth).toDouble / imageWidth
        val relativeYPosInTile = (pos / imageHeight).toDouble / imageHeight

        val absoluteXPos = relativeXPosInTile + x // column of image as fraction with offset x
        val absoluteYPos = relativeYPosInTile + y // row of image as fraction with offset y

        pos->Tile(absoluteXPos, absoluteYPos, zoom).location
      }
    ).sortBy(_._1)

    assert(method1 === method2)
  }

}
