package observatory

import java.sql.Timestamp
import java.time.LocalDate
import java.util.concurrent.ConcurrentLinkedQueue

import com.sksamuel.scrimage.{Image, Pixel}
import org.apache.commons.collections.buffer.SynchronizedBuffer

import math._
import scala.collection._
import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.ParIterable
import scala.collection.parallel.immutable.ParSeq

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

//  val timings = new ConcurrentLinkedQueue[(String, Long)]


  def distanceTemperatureCombi(temperatures: Iterable[(Location, Double)], location: Location): Iterable[(Double, Double)] = {
    temperatures.map {
      case (otherLocation, temperature) => (location.point greatCircleDistance otherLocation.point, temperature)
    }
  }

  /**
    * https://en.wikipedia.org/wiki/Inverse_distance_weighting
    *
    * @param distanceTemperatureCombinations
    * @param power
    * @return
    */
  def inverseDistanceWeighted(distanceTemperatureCombinations: Iterable[(Double, Double)], power: Int): Double = {
    val (weightedSum, inverseWeightedSum) = distanceTemperatureCombinations
      .aggregate((0.0, 0.0))(
        {
          case ((ws, iws), (distance, temp)) => {
            val w = 1 / pow(distance, power)
            (w * temp + ws, w + iws)
          }
        }, {
          case ((wsA, iwsA), (wsB, iwsB)) => (wsA + wsB, iwsA + iwsB)
        }
      )


    weightedSum / inverseWeightedSum
  }


  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    /*val exactTemperature = temperatures
      .par
      .filter(_._1 == location)
      .map(_._2)
      .headOption

    val res =exactTemperature match {
      case Some(temp) => temp
      case _ => inverseDistanceWeighted(distanceTemperatureCombi(temperatures, location), power = 3)
    }*/

    inverseDistanceWeighted(distanceTemperatureCombi(temperatures, location), power = 3)
  }


  def linearInterpolationValue(pointValueMin: Double, pointValueMax: Double, value: Double)(colorValueMin: Int, colorValueMax: Int): Int = {
    val factor = (value - pointValueMin) / (pointValueMax - pointValueMin)

    (colorValueMin + (colorValueMax - colorValueMin) * factor).toInt
  }

  def linearInterpolation(pointA: Option[(Double, Color)], pointB: Option[(Double, Color)], value: Double): Color = (pointA, pointB) match {
      case (Some((pAValue, pAColor)), Some((pBValue, pBColor))) => {
        val li = linearInterpolationValue(pAValue, pBValue, value) _
        Color(
          li(pAColor.red, pBColor.red),
          li(pAColor.green, pBColor.green),
          li(pAColor.blue, pBColor.blue)
        )
      }
      case (Some(pA), None) => pA._2
      case (None, Some(pB)) => pB._2
      case _ => Color(0, 0, 0)
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    points.find(_._1 == value) match {
      case Some((_, color)) => color
      case None => {
        val (smaller, greater) = points.toList.sortBy(_._1).partition(_._1 < value)
        linearInterpolation(smaller.reverse.headOption, greater.headOption, value)
      }
    }
  }

  def posToLocation(imageWidth: Int, imageHeight: Int)(pos: Int): Location = {
    val widthFactor = 180 * 2 / imageWidth.toDouble
    val heightFactor = 90 * 2 / imageHeight.toDouble

    val x: Int = pos % imageWidth
    val y: Int = pos / imageWidth

    Location(90 - (y * heightFactor), (x * widthFactor) - 180)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val imageWidth = 360
    val imageHeight = 180

    val locationMap = posToLocation(imageWidth, imageHeight) _

    val pixels = (0 until imageHeight * imageWidth).par.map {
      pos => pos -> interpolateColor(
          colors,
          predictTemperature(
            temperatures,
            locationMap(pos)
          )
        ).pixel
      }
      .seq
      .sortBy(_._1)
      .map(_._2)


//    var l = timings.toArray().map {
//      case (s: String, l: Long) => (s, l)
//    }.groupBy(_._1).mapValues(_.foldLeft((0L, 0)) {
//      case ((s, c), (_, l)) => (s + l, c + 1)
//    }).map {
//      case (s, (l, i)) => (s, l, i, l / i.toDouble)
//    }
//
//    l.foreach(println)


    Image(imageWidth, imageHeight, pixels.toArray)
  }

}

