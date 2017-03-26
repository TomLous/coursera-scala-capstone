package observatory

import java.time.LocalDate

import util.SparkJob
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, IntegerType}

/**
  * 1st milestone: data extraction
  */
object Extraction extends SparkJob {

  import spark.implicits._


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stations = spark
      .read
      .csv(stationsFile)
      .select(
        concat_ws("~", '_c0, '_c1).alias("id"),
        '_c2.alias("latitude").cast(DoubleType),
        '_c3.alias("longitude").cast(DoubleType)
      )
      .where('_c2.isNotNull && '_c3.isNotNull && '_c2 =!= 0.0 && '_c3 =!= 0.0)
      .persist()

    val temperatures = spark
      .read
      .csv(temperaturesFile)
      .select(
        concat_ws("~", '_c0, '_c1).alias("id"),
        '_c3.alias("day").cast(IntegerType),
        '_c2.alias("month").cast(IntegerType),
        lit(year).as("year"),
        (('_c4 - 32) / 9 * 5).alias("temperature").cast(DoubleType)
      )
      .where('_c4.between(-200, 200))



    val joined = stations
      .join(temperatures, usingColumn = "id")
      // id, lat, long, day, month, year, temp
      .map(row => {
        (
          (row.getInt(3), row.getInt(4), row.getInt(5)),
          Location(row.getDouble(1), row.getDouble(2)),
          row.getDouble(6))
      })


    joined.collect().map{
      case ((day, month, year), location, temperature) => (LocalDate.of(year,month,day), location, temperature)
    }

  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

}
