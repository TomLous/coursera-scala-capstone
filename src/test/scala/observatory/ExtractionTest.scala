package observatory

import org.apache.spark.sql.Dataset
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import util.SparkJob
import util.Resources._

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite with SparkJob {

  val year = 1975

  val stationsPath:String = resourcePath("/stations.csv")
  val temperaturePath:String = resourcePath(s"/$year.csv")

  lazy val stations:Dataset[Station] = Extraction.stations(stationsPath).persist
  lazy val temperatures:Dataset[TemperatureRecord] = Extraction.temperatures(year, temperaturePath).persist
  lazy val joined:Dataset[JoinedFormat] = Extraction.joined(stations, temperatures).persist

  lazy val test = Extraction.locateTemperatures(year, stationsPath, temperaturePath)

  test("stations"){
    assert(stations.filter((station:Station) => station.id=="007005").count()===0,"id: 007005")
    assert(stations.filter((station:Station) => station.id=="007018").count()===0,"id: 007018")
    assert(stations.filter((station:Station) => station.id=="725346~94866").count()===1,"id: 725346~94866")
    assert(stations.filter((station:Station) => station.id=="725346").count()===1,"id: 725346")
    assert(stations.filter((station:Station) => station.id=="~68601").count()===1,"id: ~68601")
  }

  test("temperatures"){
    assert(temperatures.filter((tr:TemperatureRecord) => tr.id=="010010").count()===363,"id: 010010")
    assert(temperatures.filter((tr:TemperatureRecord) => tr.id=="010010" && tr.day==1 && tr.month==1 && tr.temperature == (23.2-32)/9*5).count()===1,"id: 010010")
  }

  test("joined"){
    assert(joined.filter((jf:JoinedFormat) => jf.date == StationDate(1,1,1975) && jf.location==Location(70.933,-008.667)).count()===1,"id: 010010 ")
    assert(joined.filter((jf:JoinedFormat) => jf.date == StationDate(1,1,1975) && jf.location==Location(70.933,-008.666)).count()===0,"no loc ")
  }
}