package observatory

import com.sksamuel.scrimage.Image
import observatory.Visualization.interpolateColor

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

    val pixels = (0 until imageWidth * imageHeight)
      .par.map(pos => {
      val relXPos = (pos % imageWidth).toDouble / imageWidth
      val relYPos = (pos / imageHeight).toDouble / imageHeight

      val gridValues: Map[(Int, Int), Double] = {
        for {
          tileX <- 0 to 1
          tileY <- 0 to 1
        } yield (tileX, tileY) -> Tile(tileX, tileY, zoom).applyGrid(grid)
      }.toMap

      pos -> interpolateColor(
        colors,
        bilinearInterpolation(relXPos, relYPos, gridValues((0,0)), gridValues((0,1)), gridValues((1,0)), gridValues((1,1)))
      ).pixel(127)
    })
      .seq
      .sortBy(_._1)
      .map(_._2)


    Image(imageWidth, imageHeight, pixels.toArray)

  }

}
