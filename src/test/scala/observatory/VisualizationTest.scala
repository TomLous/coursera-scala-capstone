package observatory


import observatory.utils.SparkJob
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers with SparkJob {

  val year = 1975
  val debug = true

  val stationsPath:String = "/stations.csv"
  val temperaturePath:String = s"/$year.csv"

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)

  test("locationYearlyAverageRecords"){
    if(debug) locateAverage.take(20).foreach(println)
    assert(locateAverage.count(_._1==Location(70.933,-8.667)) === 1)
    assert(locateAverage.size === 8251)
  }

  /*
   (Location(67.55,-63.783),-6.654451137884884)
(Location(45.933,126.567),5.439407814407809)
(Location(28.967,118.867),17.528388278388274)
(Location(40.45,75.383),0.8809523809523812)
(Location(30.533,38.9),19.78724053724053)
(Location(39.083,-76.767),18.11070707070708)
(Location(42.0,15.0),10.499999999999998)
    */

  test("Finally") {
    //    System.exit(0)
  }

}
