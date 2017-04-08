package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)
  val year = 1975
  val debug = true
  val stationsPath: String = "/stations.csv"
  val temperaturePath: String = s"/$year-sample50k.csvg"

  test("tileLocation") {
    assert(Interaction.tileLocation(0, 0, 0) === Location(85.05112877980659, -180.0))
    assert(Interaction.tileLocation(10, 10, 10) === Location(84.7383871209534, -176.484375))
    assert(Interaction.tileLocation(5, 100, 100) === Location(-89.99999212633796, 945.0))
  }

  ignore("Tile range") {
    val imageWidth = 10
    val imageHeight = 10

    val zoom = 3
    val x = 0
    val y = 0

    val pixels = (0 until imageWidth * imageHeight)
      .map(pos => {
        val xPos = (pos % imageWidth).toDouble / imageWidth + x // column of image as fraction with offset x
        val yPos = (pos / imageHeight).toDouble / imageHeight + y // row of image as fraction with offset y

        pos -> ((x, y, xPos, yPos, Tile(xPos, yPos, zoom).location))

      })


    pixels.foreach(println)

  }

  test("visualize") {
    val palette = List(
      (60.0, Color(255, 255, 255)),
      (32.0, Color(255, 0, 0)),
      (12.0, Color(255, 255, 0)),
      (0.0, Color(0, 255, 255)),
      (-15.0, Color(0, 0, 255)),
      (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)),
      (-60.0, Color(0, 0, 0))
    )

    val img = Interaction.tile(locateAverage, palette, 2, 1, 1)

    img.output(new java.io.File(s"../../src/test/resources/tile-$year-sample50k-2-1-1.png"))

    assert(img.pixels.length === 256 * 256)
  }

}
