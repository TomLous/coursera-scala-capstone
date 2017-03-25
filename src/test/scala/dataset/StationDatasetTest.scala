package dataset

import model.Station
import observatory.Location
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
class StationDatasetTest  extends FunSuite with Checkers with SparkJob {

  lazy val stations = StationDataset(resourcePath("/stations.csv"))

  test("show"){
    stations.show()
  }

  test("get first Station record"){
    assert(stations.head() === Station(Some("007005"),None,None,None))
  }

  test("composedIds"){
    val s = stations
      .filter((station:Station) => station.WBANid.isDefined).head()
    assert(s === Station(Some("035770"), Some("35046"), Some(52.367), Some(0.483)))
    assert(s.composedId === "035770*35046")
    assert(s.location === Some(Location(52.367,0.483)))
    //    stations.take()
  }
}
