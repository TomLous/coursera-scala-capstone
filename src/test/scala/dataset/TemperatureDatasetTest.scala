package dataset

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import util.Resources.resourcePath
import util.SparkJob

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom lous
  */
class TemperatureDatasetTest extends FunSuite with Checkers with SparkJob {


  lazy val temperartures = TemperatureDataset(resourcePath("/1975.csv"), 1975)

  test("show"){
    temperartures.show()
  }


}
