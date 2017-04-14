package observatory

import com.sksamuel.scrimage.Image
import observatory.Visualization.interpolateColor

import scala.collection.immutable
import scala.math._

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    d00 * (1 - x) * (1 - y) +
    d10 *      x  * (1 - y) +
    d01 * (1 - x) *      y  +
    d11 *      x  *      y
  }


  def tileLocations(offsetX: Int, offsetY: Int, zoom: Int, imageWidth: Int, imageHeight: Int):immutable.IndexedSeq[(Int, Location)] = {
    for{
      xPixel <- 0 until imageWidth
      yPixel <- 0 until imageHeight
    } yield xPixel + yPixel * imageWidth -> Tile(xPixel.toDouble / imageWidth + offsetX, yPixel.toDouble / imageHeight + offsetY, zoom).location
  }


  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
    val imageWidth = 256
    val imageHeight = 256

    val pixels = tileLocations(x, y, zoom, imageWidth, imageHeight).par.map{
      case (pos,pixelLocation) => {

        val d00 = grid(ceil(pixelLocation.lat).toInt, floor(pixelLocation.lon).toInt) // nw
        val d01 = grid(floor(pixelLocation.lat).toInt, floor(pixelLocation.lon).toInt) // sw
        val d10 = grid(ceil(pixelLocation.lat).toInt, ceil(pixelLocation.lon).toInt) // ne
        val d11 = grid(floor(pixelLocation.lat).toInt, ceil(pixelLocation.lon).toInt) // se

        val xFraction = pixelLocation.lon - floor(pixelLocation.lon)
        val yFraction = ceil(pixelLocation.lat) - pixelLocation.lat


        pos -> interpolateColor(
          colors,
          bilinearInterpolation(x=xFraction, y=yFraction, d00=d00, d01=d01, d10=d10, d11=d11)
        ).pixel(127)
      }}
      .seq
      .sortBy(_._1)
      .map(_._2)


    Image(imageWidth, imageHeight, pixels.toArray)

  }

}
