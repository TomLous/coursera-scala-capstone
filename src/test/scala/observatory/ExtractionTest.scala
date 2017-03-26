package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import util.SparkJob
import util.Resources._

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with SparkJob {

  val stationsPath:String = resourcePath("/stations.csv")

  test("locateTemperatures"){
    val year = 1975

    val test = Extraction.locateTemperatures(year, stationsPath, resourcePath(s"/$year.csv"))

    test.take(10).foreach(println)
  }
}