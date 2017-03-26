package observatory

import java.time.LocalDate

case class Location(lat: Double, lon: Double)

case class Color(red: Int, green: Int, blue: Int)

case class Joined(id: String, latitude:Double, longitude: Double, day: Int, month: Int, year: Int, temperature: Double)

case class StationDate(day: Int, month: Int, year: Int){
  def toLocalDate = LocalDate.of(year, month, day)
}

case class JoinedFormat(date: StationDate, location: Location, temperature: Double)


case class Station(id: String, latitude: Double, longitude: Double)

case class TemperatureRecord(id: String, day: Int, month: Int, year: Int, temperature: Double)