package model

import java.time.LocalDate

import observatory.Location

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom Lous
  */
case class LocationTemperatureRecord(
                                      date: LocalDate,
                                      location: Location,
                                      celsius: Double
                                    )
