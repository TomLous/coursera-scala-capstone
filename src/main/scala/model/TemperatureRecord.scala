package model

import java.time.LocalDate

import scala.util.Try

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Tom Lous 
  */
case class TemperatureRecord(
                              id: String,
                              STNid: Option[String],
                              WBANid: Option[String],
                              day: Int,
                              month: Int,
                              year: Int,
                              fahrenheit: Option[Fahrenheit]
                            ){
  val composedId:String = STNid.getOrElse("") + "*" + WBANid.getOrElse("")

  val localDate:Option[LocalDate] = Try(LocalDate.of(year,month,day)).toOption

//  val celsius:Option[Double] = fahrenheit match {
//    case Some(t) if t > -130.0 && t < 212 => Some((t - 32) * 5 / 9)
//    case _ => None
//  }

}

object TemperatureRecord{
   def apply(STNid: Option[String],
    WBANid: Option[String],
    day: Int,
    month: Int,
    year: Int,
    fahrenheit: Option[Fahrenheit]):TemperatureRecord = TemperatureRecord(
     composeId(STNid, WBANid), STNid, WBANid, day, month, year, fahrenheit)

  def composeId(STNid: Option[String], WBANid: Option[String]) = STNid.getOrElse("") + "*" + WBANid.getOrElse("")
}
