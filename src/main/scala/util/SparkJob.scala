package util

import org.apache.spark.sql.SparkSession

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
trait SparkJob {

  implicit val spark:SparkSession = SparkSession
    .builder()
    .master("local[4]")
    .appName(this.getClass.getSimpleName)
    .getOrCreate()

}
