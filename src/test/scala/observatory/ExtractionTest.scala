package observatory

import org.apache.spark.sql.Dataset
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import utils.SparkJob
import utils.Resources._

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with SparkJob {

  val year = 1975
  val debug = true

  val stationsPath:String = "/stations.csv"
  val temperaturePath:String = s"/$year.csv"

  lazy val stations:Dataset[Station] = Extraction.stations(stationsPath).persist
  lazy val temperatures:Dataset[TemperatureRecord] = Extraction.temperatures(year, temperaturePath).persist
  lazy val joined:Dataset[JoinedFormat] = Extraction.joined(stations, temperatures).persist

  lazy val locateTemperatures = Extraction.locateTemperatures(year, stationsPath, temperaturePath)
  lazy val locateAverage = Extraction.locationYearlyAverageRecords(locateTemperatures)

  test("stations"){
    if(debug) stations.show()
    assert(stations.filter((station:Station) => station.id=="007005").count()===0,"id: 007005")
    assert(stations.filter((station:Station) => station.id=="007018").count()===0,"id: 007018")
    assert(stations.filter((station:Station) => station.id=="725346~94866").count()===1,"id: 725346~94866")
    assert(stations.filter((station:Station) => station.id=="725346").count()===1,"id: 725346")
    assert(stations.filter((station:Station) => station.id=="~68601").count()===1,"id: ~68601")
    assert(stations.count()===27708,"Num stations")
  }

  test("temperatures"){
    if(debug) temperatures.show()
    assert(temperatures.filter((tr:TemperatureRecord) => tr.id=="010010").count()===363,"id: 010010")
    assert(temperatures.filter((tr:TemperatureRecord) => tr.id=="010010" && tr.day==1 && tr.month==1 && tr.temperature == (23.2-32)/9*5).count()===1,"id: 010010")
  }

  test("joined"){
    if(debug) joined.show()
    assert(joined.filter((jf:JoinedFormat) => jf.date == StationDate(1,1,1975) && jf.location==Location(70.933,-008.667)).count()===1,"id: 010010 ")
    assert(joined.filter((jf:JoinedFormat) => jf.date == StationDate(1,1,1975) && jf.location==Location(70.933,-008.666)).count()===0,"no loc ")
  }

  test("locateTemperatures"){
    if(debug) locateTemperatures.take(20).foreach(println)
    assert(locateTemperatures.count(_._2==Location(70.933,-8.667)) === 363)
    assert(locateTemperatures.size === 2176493)
  }

  test("locationYearlyAverageRecords"){
    if(debug) locateAverage.take(20).foreach(println)
    assert(locateAverage.count(_._1==Location(70.933,-8.667)) === 1)
    assert(locateAverage.size === 8251)
  }

  test("Finally") {
//    System.exit(0)
  }
}