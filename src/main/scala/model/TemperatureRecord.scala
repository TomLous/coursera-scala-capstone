package model

import java.time.LocalDate

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
case class TemperatureRecord(
                              STNid: String,
                              WBANid: Option[String],
                              month: Int,
                              day: Int,
                              year: Int,
                              fahrenheit: Option[Double]
                            ){
  val composedId:String = STNid + WBANid.getOrElse("")

  val localDate:LocalDate = LocalDate.of(year,month,day)

  val temperature:Option[Double] = fahrenheit match {
    case Some(t) if t > -130 && t < 57 => Some(t)
    case _ => None
  }

}
