package model

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom Lous
  */
@RunWith(classOf[JUnitRunner])
class TemperatureRecordTest extends FunSuite with Checkers {

  test("TemperatureRecord 1"){
    val t = TemperatureRecord(Some("a"),Some("b"),1,1,2017, Some(Fahrenheit(80.4)))
    assert(t.composedId === "a*b")
    assert(t.fahrenheit.map(_.toCelsius) === Some(Fahrenheit(80.4).toCelsius))
    assert(t.localDate === Some(LocalDate.of(2017,1,1)))

  }

  test("TemperatureRecord 2"){
    val t = TemperatureRecord(None,Some("b"),1,12,2017, Some(Fahrenheit(+80.4)))
    assert(t.composedId === "*b")
    assert(t.fahrenheit.map(_.toCelsius) === Some(Fahrenheit(80.4).toCelsius))
    assert(t.localDate === Some(LocalDate.of(2017,12,1)))

  }

  test("TemperatureRecord 3"){
//    val t = TemperatureRecord(Some("a"),None,31,2,2017, Some(-280.4))
//    assert(t.composedId === "a*")
//    assert(t.celsius === None)
//    assert(t.localDate === None)

  }
}
