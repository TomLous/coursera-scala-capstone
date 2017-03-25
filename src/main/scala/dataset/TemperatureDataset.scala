package dataset

import model.{Station, TemperatureRecord}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Tom Lous
  */
object TemperatureDataset {
  type TemperatureDataset = Dataset[TemperatureRecord]

  def apply(filePath: String, year:Int)(implicit spark: SparkSession): TemperatureDataset = {
    import spark.implicits._

    spark
      .read

      .option("header", false)
      .option("sep", ",")
      .option("ignoreLeadingWhiteSpace", true)
      .option("ignoreTrailingWhiteSpace", true)
      .option("quote", "")
      .option("nullValue", "")
      .option("mode", "FAILFAST")

      .csv(filePath)

      .select(
        '_c0.alias("STNid"),
        '_c1.alias("WBANid"),
        '_c3.alias("day").cast(IntegerType),
        '_c2.alias("month").cast(IntegerType),
        lit(year).alias("year"),
        '_c4.alias("fahrenheit").cast(DoubleType)

      )

      .as[TemperatureRecord]

  }

}
