package model

import observatory.Location

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
case class Station(
                    STNid: String,
                    WBANid: Option[String],
                    latitude: Option[Double],
                    longitude: Option[Double]
                  ){
  val composedId:String = STNid + WBANid.getOrElse("")

  val location:Option[Location] = (latitude, longitude) match {
    case (Some(lat), Some(lon)) => Some(Location(lat, lon))
    case _ => None
  }
}

