package observatory

import java.time.LocalDate

import com.sksamuel.scrimage.{Pixel, RGBColor}

import scala.math._

case class Location(lat: Double, lon: Double) {
  lazy val point:Point = Point(toRadians(lat), toRadians(lon))

}

case class Point(ϕ: Double, λ: Double) {
  lazy val location:Location = Location(toDegrees(ϕ), toDegrees(λ))

  /**
    * Added for special case: https://www.coursera.org/learn/scala-capstone/discussions/weeks/2/threads/YY0u6Ax8EeeqzRJFs29uDA
    *
    * @param other Point for distance calculatuion
    * @return distance on earth in meters
    */
  def haversineEarthDistance(other: Point): Double = {
    var r = 6372.8 // mean radius Earth in KM
    r * greatCircleDistance(other) * 1000
  }

  /**
    * https://en.wikipedia.org/wiki/Great-circle_distance#Computational_formulas
    *
    * @param other Point for distance calculatuion
    * @return distance in radians
    */
  def greatCircleDistance(other: Point): Double = {
    val Δϕ = abs(other.ϕ - ϕ)
    val Δλ = abs(other.λ - λ)

    val a =  pow(sin(Δϕ / 2), 2) + cos(ϕ) * cos(other.ϕ) * pow(sin(Δλ / 2), 2)
    2 * atan2(sqrt(a), sqrt(1 - a))
  }

}


case class Color(red: Int, green: Int, blue: Int) {
  lazy val pixel = Pixel(RGBColor(red, green, blue))
}

case class Joined(id: String, latitude:Double, longitude: Double, day: Int, month: Int, year: Int, temperature: Double)

case class StationDate(day: Int, month: Int, year: Int){
  def toLocalDate = LocalDate.of(year, month, day)
}

case class JoinedFormat(date: StationDate, location: Location, temperature: Double)


case class Station(id: String, latitude: Double, longitude: Double)

case class TemperatureRecord(id: String, day: Int, month: Int, year: Int, temperature: Double)