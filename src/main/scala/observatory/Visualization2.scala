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


  /**
    * Generates a sequence of pos, location  tuples for a Tile image
    * @param offsetX TL Xpos of Tile
    * @param offsetY TL YPos of Tile
    * @param zoom zoom level of tile
    * @param imageWidth in pixels
    * @param imageHeight in pixels
    * @return 'Map' of (pos->Location) within the tile
    */
  def pixelLocations(offsetX: Int, offsetY: Int, zoom: Int, imageWidth: Int, imageHeight: Int):immutable.IndexedSeq[(Int, Location)] = {
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

    val pixels = pixelLocations(x, y, zoom, imageWidth, imageHeight).par.map{
      case (pos,pixelLocation) => {

        val latRange = List(floor(pixelLocation.lat).toInt, ceil(pixelLocation.lat).toInt)
        val lonRange = List(floor(pixelLocation.lon).toInt, ceil(pixelLocation.lon).toInt)

        val d = {
          for {
            xPos <- 0 to 1
            yPos <- 0 to 1
          } yield (xPos, yPos) -> grid(latRange(1 - yPos), lonRange(xPos))
        }.toMap

        val xFraction = pixelLocation.lon - lonRange(0)
        val yFraction = latRange(1) - pixelLocation.lat


        pos -> interpolateColor(
          colors,
          bilinearInterpolation(x=xFraction, y=yFraction, d00=d((0,0)), d01=d((0,1)), d10=d((1,0)), d11=d((1,1)))
        ).pixel(127)
      }}
      .seq
      .sortBy(_._1)
      .map(_._2)


    Image(imageWidth, imageHeight, pixels.toArray)
  }

}
