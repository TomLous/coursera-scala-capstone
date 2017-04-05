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


  test("distanceTemperatureCombi"){
    assert(Visualization.distanceTemperatureCombi(List((Location(10,10), 10),(Location(30,30),30)), Location(20,20)).toList.sortBy(_._2) === List((0.2424670477208617,10.0),(0.23530045911535308,30.0)))
  }

  test("inverseDistanceWeighted"){
    assert(Visualization.inverseDistanceWeighted(Visualization.distanceTemperatureCombi(List((Location(10,10), 10),(Location(30,30),30)), Location(20,20)), 3) === 20.449734945928157)
    assert(Visualization.inverseDistanceWeighted(Visualization.distanceTemperatureCombi(List((Location(10,10), 10),(Location(10,30),30), (Location(10,30),20)), Location(30,10)), 3) === 16.584950329573303)
  }

  test("Distance 0.0"){
    assert(Visualization.predictTemperature(locateAverage, Location(67.55,-63.783)) === -6.654451137884884)
    assert(Visualization.predictTemperature(locateAverage, Location(39.083,-76.767)) === 18.11070707070708)
  }

  test("Distance != 0.0"){
    assert(Visualization.predictTemperature(locateAverage, Location(52.0,4.5)).round === 10)
    assert(Visualization.predictTemperature(locateAverage, Location(4.5,52.0)).round === 19)
  }

  test("linearInterpolationValue"){
    assert(Visualization.linearInterpolationValue(0,10,5)(0,100) === 50)
    assert(Visualization.linearInterpolationValue(2,12,7)(0,100) === 50)
    assert(Visualization.linearInterpolationValue(2,12,7)(10,20) === 15)
    assert(Visualization.linearInterpolationValue(0,10,1)(10,20) === 11)
    assert(Visualization.linearInterpolationValue(0,20,3)(10,20) === 11)
  }

  test("linearInterpolation"){
    assert(Visualization.linearInterpolation(Some((0,Color(0,0,0))), Some((100, Color(255, 255, 255))), 50) === Color(127,127,127))
    assert(Visualization.linearInterpolation(Some((0,Color(0,0,0))), Some((80, Color(255, 255, 255))), 10) === Color(31,31,31))
    assert(Visualization.linearInterpolation(Some((0,Color(255,127,0))), Some((80, Color(0, 127, 255))), 10) === Color(223,127,31))
  }

  test("interpolateColor"){
    val palette = List(
      (100.0, Color(255, 255, 255)),
      (50.0, Color(0, 0, 0)),
      (0.0, Color(255, 0, 127))
    )

    assert(Visualization.interpolateColor(palette, 50.0) === Color(0, 0, 0))
    assert(Visualization.interpolateColor(palette, 0.0) === Color(255, 0, 127))
    assert(Visualization.interpolateColor(palette, -10.0) === Color(255, 0, 127))
    assert(Visualization.interpolateColor(palette, 200.0) === Color(255, 255, 255))
    assert(Visualization.interpolateColor(palette, 75.0) === Color(127, 127, 127))
    assert(Visualization.interpolateColor(palette, 25.0) === Color(127, 0, 63))
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
