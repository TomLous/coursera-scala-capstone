package observatory


import observatory.utils.SparkJob
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers with SparkJob {

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)
  val year = 1975
  val debug = true
  val stationsPath: String = "/stations.csv"
  val temperaturePath: String = s"/$year-sample50k.csvg"


  test("locationYearlyAverageRecords") {
    if (debug) locateAverage.take(20).foreach(println)
    assert(locateAverage.count(_._1 == Location(70.933, -8.667)) === 1)
    assert(locateAverage.size === 215)
  }


  test("distanceTemperatureCombi") {
    assert(Visualization.distanceTemperatureCombi(List((Location(10, 10), 10), (Location(30, 30), 30)), Location(20, 20)).toList.sortBy(_._2) === List((0.2424670477208617 * 6372.8 * 1000, 10.0), (0.23530045911535308 * 6372.8 * 1000, 30.0)))
  }

  test("inverseDistanceWeighted") {
    assert(Visualization.inverseDistanceWeighted(Visualization.distanceTemperatureCombi(List((Location(10, 10), 10), (Location(30, 30), 30)), Location(20, 20)), 3).round === 20)
    assert(Visualization.inverseDistanceWeighted(Visualization.distanceTemperatureCombi(List((Location(10, 10), 10), (Location(10, 30), 30), (Location(10, 30), 20)), Location(30, 10)), 3).round === 17)
  }

  /*test("Distance 0.0") {
    assert(Visualization.predictTemperature(locateAverage, Location(67.55, -63.783)) === -6.654451137884884)
    assert(Visualization.predictTemperature(locateAverage, Location(39.083, -76.767)) === 18.11070707070708)
  }

  test("Distance != 0.0") {
    assert(Visualization.predictTemperature(locateAverage, Location(52.0, 4.5)).round === 10)
    assert(Visualization.predictTemperature(locateAverage, Location(4.5, 52.0)).round === 19)
  }*/

  test("Distance 0.0") {
    assert(Visualization.predictTemperature(locateAverage, Location(67.55, -63.783)).round === 4)
    assert(Visualization.predictTemperature(locateAverage, Location(39.083, -76.767)).round === 13)
  }

  test("Distance != 0.0") {
    assert(Visualization.predictTemperature(locateAverage, Location(52.0, 4.5)).round === 6)
    assert(Visualization.predictTemperature(locateAverage, Location(4.5, 52.0)).round === 13)
    assert(Visualization.predictTemperature(locateAverage, Location(0.0, 0.0)).round === 7)

  }

  test("predictTemperature small sets") {
    assert(Visualization.predictTemperature(List((Location(45.0, -90.0), 10.0), (Location(-45.0, 0.0), 20.0)), Location(0.0, -45.0)) === 15.0)
    assert(Visualization.predictTemperature(List((Location(0.0, 0.0), 10.0)), Location(0.0, 0.0)) === 10.0)
    assert(Visualization.predictTemperature(List((Location(45.0, -90.0), 0.0), (Location(-45.0, 0.0), 59.028308521858634)), Location(0.0, 0.0)).round === 52)
  }

  test("linearInterpolationValue") {
    assert(Visualization.linearInterpolationValue(0, 10, 5)(0, 100) === 50)
    assert(Visualization.linearInterpolationValue(2, 12, 7)(0, 100) === 50)
    assert(Visualization.linearInterpolationValue(2, 12, 7)(10, 20) === 15)
    assert(Visualization.linearInterpolationValue(0, 10, 1)(10, 20) === 11)
    assert(Visualization.linearInterpolationValue(0, 20, 3)(10, 20) === 12)
  }

  test("linearInterpolation") {
    assert(Visualization.linearInterpolation(Some((0, Color(0, 0, 0))), Some((100, Color(255, 255, 255))), 50) === Color(128, 128, 128))
    assert(Visualization.linearInterpolation(Some((0, Color(0, 0, 0))), Some((80, Color(255, 255, 255))), 10) === Color(32, 32, 32))
    assert(Visualization.linearInterpolation(Some((0, Color(255, 128, 0))), Some((80, Color(0, 128, 255))), 10) === Color(223, 128, 32))
  }

  test("interpolateColor") {
    val palette = List(
      (100.0, Color(255, 255, 255)),
      (50.0, Color(0, 0, 0)),
      (0.0, Color(255, 0, 128))
    )

    assert(Visualization.interpolateColor(palette, 50.0) === Color(0, 0, 0))
    assert(Visualization.interpolateColor(palette, 0.0) === Color(255, 0, 128))
    assert(Visualization.interpolateColor(palette, -10.0) === Color(255, 0, 128))
    assert(Visualization.interpolateColor(palette, 200.0) === Color(255, 255, 255))
    assert(Visualization.interpolateColor(palette, 75.0) === Color(128, 128, 128))
    assert(Visualization.interpolateColor(palette, 25.0) === Color(128, 0, 64))
  }

  test("posToLocation") {
    assert(Visualization.posToLocation(360,180)(0) === Location(90.0, -180.0))
    assert(Visualization.posToLocation(360,180)(32580) === Location(0.0,0.0))
    assert(Visualization.posToLocation(360,180)(64799) === Location(-89.0, 179.0))
    assert(Visualization.posToLocation(720,360)(360) === Location(90.0,0.0))
    assert(Visualization.posToLocation(720,360)(129960) === Location(0.0,0.0))
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

    val img = Visualization.visualize(locateAverage, palette)

    img.output(new java.io.File(s"../../src/test/resources/$year-sample50k.png"))

    assert(img.pixels.length === 360 * 180)
  }



  test("Finally") {
    //    System.exit(0)
  }

}
