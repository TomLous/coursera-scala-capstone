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


    gridpoints.foreach(println)
  }

}