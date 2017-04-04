package observatory

import java.time.LocalDate

import utils.SparkJob
import utils.Resources._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, IntegerType}


/**
  * 1st milestone: data extraction
  */
object Extraction extends SparkJob {

  import spark.implicits._

  def stations(stationsFile: String): Dataset[Station] = {
    spark
      .read
      .csv(resourcePath(stationsFile))
      .select(
        concat_ws("~", coalesce('_c0, lit("")), '_c1).alias("id"),
        '_c2.alias("latitude").cast(DoubleType),
        '_c3.alias("longitude").cast(DoubleType)
      )
      .where('_c2.isNotNull && '_c3.isNotNull && '_c2 =!= 0.0 && '_c3 =!= 0.0)
    .as[Station]
  }

  def temperatures(year: Int,temperaturesFile: String): Dataset[TemperatureRecord] = {
    spark
      .read
      .csv(resourcePath(temperaturesFile) )
      .select(
        concat_ws("~", coalesce('_c0, lit("")), '_c1).alias("id"),
        '_c3.alias("day").cast(IntegerType),
        '_c2.alias("month").cast(IntegerType),
        lit(year).as("year"),
        (('_c4 - 32) / 9 * 5).alias("temperature").cast(DoubleType)
      )
      .where('_c4.between(-200, 200))
      .as[TemperatureRecord]
  }

  def joined(stations: Dataset[Station], temperatures: Dataset[TemperatureRecord]):Dataset[JoinedFormat] = {
    stations
      .join(temperatures, usingColumn = "id")
      .as[Joined]
      .map(j => (StationDate(j.day, j.month, j.year), Location(j.latitude, j.longitude), j.temperature))
      .toDF("date", "location", "temperature")
      .as[JoinedFormat]
  }


  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val j = joined(stations(stationsFile), temperatures(year, temperaturesFile))

    //    // It'stations a shame we have to use the LocalDate because Spark cannot encode that. hence this ugly bit
    j.collect()
      .par
      .map(
        jf => (jf.date.toLocalDate, jf.location, jf.temperature)
      ).seq
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    records
      .par
      .groupBy(_._2)
      .mapValues(
        l => l.foldLeft(0.0)(
          (t,r) => t + r._3) / l.size
      )
      .seq
  }

}
