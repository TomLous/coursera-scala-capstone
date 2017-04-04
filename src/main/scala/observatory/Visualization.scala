package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import math._


import scala.collection.parallel.ParIterable

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  def distanceTemperatureCombi(temperatures: Iterable[(Location, Double)], location: Location): ParIterable[(Double, Double)] = temperatures.par.map {
      case (otherLocation, temperature) => (location.point greatCircleDistance otherLocation.point, temperature)
  }

  /**
    * https://en.wikipedia.org/wiki/Inverse_distance_weighting
    * @param distanceTemperatureCombinations
    * @param power
    * @return
    */
  def inverseDistanceWeighted(distanceTemperatureCombinations: ParIterable[(Double, Double)], power: Int): Double = {

    val (weightedSum, inverseWeightedSum) = distanceTemperatureCombinations
      .aggregate((0.0, 0.0))(
        {
          case ((ws, iws), (distance, temp)) => {
            val w = 1 / pow(distance, power)
            (w * temp + ws, w + iws)
          }
        },
        {
          case ((wsA, iwsA),(wsB, iwsB)) => (wsA + wsB, iwsA + iwsB)
        }
      )

    weightedSum / inverseWeightedSum
  }


  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val exactTemperature = temperatures
      .par
      .filter(_._1 == location)
      .map(_._2)
      .headOption

    exactTemperature match {
      case Some(temp) => temp
      case _ => inverseDistanceWeighted(distanceTemperatureCombi(temperatures, location), power=3)
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

