package model

import java.time.LocalDate

import scala.util.Try

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
case class TemperatureRecord(
                              STNid: Option[String],
                              WBANid: Option[String],
                              day: Int,
                              month: Int,
                              year: Int,
                              fahrenheit: Option[Double]
                            ){
  val composedId:String = STNid.getOrElse("") + "*" + WBANid.getOrElse("")

  val localDate:Option[LocalDate] = Try(LocalDate.of(year,month,day)).toOption

  val temperature:Option[Double] = fahrenheit match {
    case Some(t) if t > -130.0 && t < 212 => Some(t)
    case _ => None
  }

}
