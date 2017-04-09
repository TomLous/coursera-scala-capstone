package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers {

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)
  val year = 1975
  val debug = true
  val stationsPath: String = "/stations.csv"
  val temperaturePath: String = s"/$year-sample50k.csvg"


  test("tileLocation") {
    val gridFetch = Manipulation.makeGrid(locateAverage)

    val gridpoints = for {
      lat <- -89 to 90
      lon <- -180 to 179
    } yield gridFetch(lat, lon)


    assert(gridpoints.size === 360 * 180)
    assert(gridpoints(360 * 180 - 1) === 0.930407896834476)
  }

  test("average") {
    val t = List(List((Location(0.0, 0.0), 10.0)), List((Location(0.2, 0.3), 20.0)), List((Location(-0.5, -0.8), 5.0)))

    val avgs = Manipulation.average(t)

    assert(avgs(0, 0) === 11.666666666666666)
  }
}