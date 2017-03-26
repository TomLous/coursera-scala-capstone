package model

/**
  * Created by Tom Lous on 26/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
case class Fahrenheit(temperature: Double) {

  def toCelsius:Celsius = Celsius((temperature - 32) / 9 * 5)

}


case class Celsius(temperature: Double) {

  def toFahrenheit:Celsius = Celsius((temperature * 9 / 5) + 32)
}
