package dataset

import model.Station
import org.apache.spark.sql.types.DoubleType
import org.apache.spark.sql.{Dataset, SparkSession}

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
object StationDataset {

  type StationDataset = Dataset[Station]

  def apply(filePath: String)(implicit spark: SparkSession): StationDataset = {
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
        '_c2.alias("latitude").cast(DoubleType),
        '_c3.alias("longitude").cast(DoubleType)
      )

      .as[Station]

  }

}
