

package observatory

import dataset.StationDataset
import model.Station
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import util.Resources.resourcePath
import util.SparkJob

@RunWith(classOf[JUnitRunner])
class DatasetTest  extends FunSuite with Checkers with SparkJob {

  lazy val stations = StationDataset(resourcePath("/stations.csv"))

  test("show"){
    stations.show()
  }

  test("get first Station record"){
    assert(stations.head() === Station(Some("007005"),None,None,None))
  }

  test("composedIds"){
//    stations.take()
  }
}
