package observatory

/**
  * 4th milestone: value-added information
  */
object Manipulation {

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Double)]): (Int, Int) => Double = {
    val grid: Map[Location, Double] = {
      for {
        lat <- -89 to 90
        lon <- -180 to 179
      } yield Location(lat, lon) -> Visualization.predictTemperature(temperatures, Location(lat, lon))
    }.toMap

    (lat, lon) => grid(Location(lat, lon))
  }


  /*{
    (lat, lon) => {
      assert(lat > -90 && lat <= 90)
      assert(lon > -180 && lat <= 180)
      val (totalTemp, totalCount) = temperatures.filter{
        case (location, _) => location.lat.toInt == lat && location.lon.toInt == lon
      }.foldLeft((0.0, 0)){
        case ((sum, count), (_, temp)) => (sum + temp, count+1)
      }
      totalTemp / totalCount
    }
  }*/

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Double)]]): (Int, Int) => Double = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A sequence of grids containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Double)], normals: (Int, Int) => Double): (Int, Int) => Double = {
    ???
  }


}

