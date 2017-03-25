package model

import observatory.Location

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Tom Lous
  */
case class Station(
                    STNid: Option[String],
                    WBANid: Option[String],
                    latitude: Option[Double],
                    longitude: Option[Double]
                  ){
  val composedId:String = STNid.getOrElse("") + "*" + WBANid.getOrElse("")

  val location:Option[Location] = (latitude, longitude) match {
    case (Some(lat), Some(lon)) if lat!=0.0 && lon!=0.0 => Some(Location(lat, lon))
    case _ => None
  }
}

