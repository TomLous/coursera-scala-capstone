package dataset

import java.time.LocalDate

import model.TemperatureRecord
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers
import util.Resources.resourcePath
import util.SparkJob

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom lous
  */
@RunWith(classOf[JUnitRunner])
class TemperatureDatasetTest extends FunSuite with Checkers with SparkJob {


  lazy val temperartures = TemperatureDataset(resourcePath("/1975.csv"), 1975)

  test("show"){
    temperartures.show()
  }

 /* test("get first Temperature record"){
    assert(temperartures.head() === TemperatureRecord(Some("010010"),None,1,1,1975,Some(23.2)))
  }

  test("composedIds"){
    val t = temperartures
      .filter((temp:TemperatureRecord) => temp.WBANid.isDefined).head()

//    println(t)
    assert(t === TemperatureRecord(Some("037970"),Some("35047"),1,1,1975,Some(45.7)))
    assert(t.composedId === "037970*35047")
    assert(t.localDate === Some(LocalDate.of(1975,1,1)))
    assert(t.celsius === Some((45.7-32)/9*5))
  }*/

}
