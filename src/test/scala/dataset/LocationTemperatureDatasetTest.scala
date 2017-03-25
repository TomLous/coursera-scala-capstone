package dataset

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import util.Resources.resourcePath
import util.SparkJob

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom Lous
  */
@RunWith(classOf[JUnitRunner])
class LocationTemperatureDatasetTest extends FunSuite with Checkers with SparkJob {

  lazy val stations = StationDataset(resourcePath("/stations.csv"))
  lazy val temperartures = TemperatureDataset(resourcePath("/1975.csv"), 1975)

  lazy val combi = LocationTemperatureDataset(stations, temperartures)

  test("show"){
    combi.show()
  }
}
