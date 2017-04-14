package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.math._

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {

  test("compare grid methods") {
    val x = 1
    val y = 1
    val zoom = 1
    val imageWidth = 5
    val imageHeight = 5


    val method1 = Visualization2.pixelLocations(x, y, zoom, imageWidth, imageHeight).sortBy(_._1)
    //    println(Visualization2.pixelLocations(0,0,0,5,5))


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

  test("compare d methods") {
    val pixelLocation = Location(82.3, -65.6)

    val (latMax, latMin) = (ceil(pixelLocation.lat), floor(pixelLocation.lat))
    val (lonMax, lonMin) = (ceil(pixelLocation.lon), floor(pixelLocation.lon))

    val d00 = (latMax.toInt, lonMin.toInt) // nw
    val d01 = (latMin.toInt, lonMin.toInt) // sw
    val d10 = (latMax.toInt, lonMax.toInt) // ne
    val d11 = (latMin.toInt, lonMax.toInt) // se

    val method1 = List((0,0)->d00, (0,1)->d01, (1,0)->d10, (1,1)->d11).toMap


    val latRange = List(floor(pixelLocation.lat).toInt, ceil(pixelLocation.lat).toInt)
    val lonRange = List(floor(pixelLocation.lon).toInt, ceil(pixelLocation.lon).toInt)

    val method2 = {
      for {
        xPos <- 0 to 1
        yPos <- 0 to 1
      } yield ((xPos, yPos), (latRange(1 - yPos), lonRange(xPos)))
    }.toMap

    assert(method1 === method2)


    val xFractionA = pixelLocation.lon - lonRange(0)
    val yFractionA = latRange(1) - pixelLocation.lat

    val xFractionB = pixelLocation.lon - lonMin
    val yFractionB = latMax - pixelLocation.lat

    assert(xFractionA === xFractionB)
    assert(yFractionA === yFractionB)

//    println(List(xFractionA, yFractionA))
//    println(List(xFractionB, yFractionB))
//
  }

  test("bilinearInterpolation"){

    assert(Visualization2.bilinearInterpolation(0.5, 0.5, 10, 20, 30, 40) === 25.0)
    assert(Visualization2.bilinearInterpolation(0.1, 0.5, 10, 20, 30, 40) === 17.0)
    assert(Visualization2.bilinearInterpolation(0.5, 0.1, 10, 20, 30, 40) === 21.0)
    assert(Visualization2.bilinearInterpolation(0.9, 0.1, 10, 20, 30, 40) === 29.0)
    assert(Visualization2.bilinearInterpolation(1.0, 0.0, 10, 20, 30, 40) === 30.0)
  }

  test("visualizeGrid"){
    //@todo not implemented :-(
  }

}
