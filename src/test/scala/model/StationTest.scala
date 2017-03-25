package model

import observatory.Location
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom Lous
  */
@RunWith(classOf[JUnitRunner])
class StationTest extends FunSuite with Checkers {

  test("Station ab"){
    val s = Station(Some("a"),Some("b"),Some(+1.2), Some(-2.3))

    assert(s.composedId == "a*b")
  }

  test("Station a"){
    val s = Station(Some("a"),None,Some(+1.2), Some(-2.3))

    assert(s.composedId == "a*")
  }

  test("Station location 1.2;-2.3"){
    val s = Station(Some("a"),None,Some(+1.2), Some(-2.3))

    assert(s.location === Some(Location(1.2, -2.3)))
  }

  test("Station location none"){
    val s = Station(Some("a"),None,Some(+1.2), None)

    assert(s.location === None)
  }

  test("Station location none (2)"){
    val s = Station(Some("a"),None,Some(+0.000), Some(-0.000))

    assert(s.location === None)
  }
}
