package observatory

import java.time.LocalDate

import util.SparkJob
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

/**
  * 1st milestone: data extraction
  */
object Extraction extends SparkJob{

  import spark.implicits._

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, celsius)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    spark
      .read

      .option("header", false)
      .option("sep", ",")
      .option("ignoreLeadingWhiteSpace", true)
      .option("ignoreTrailingWhiteSpace", true)
      .option("quote", "")
      .option("nullValue", "")
      .option("mode", "FAILFAST")


      .csv(stationsFile).toDF()

    .show()

    ???
  }

  /**
    * @param records A sequence containing triplets (date, location, celsius)
    * @return A sequence containing, for each location, the average celsius over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    ???
  }

}
