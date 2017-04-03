package observatory.utils

import org.apache.log4j.Logger
import org.apache.log4j.Level

import org.apache.spark.sql.SparkSession

import scala.util.Try

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Tom Lous
  */
trait SparkJob {

  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  implicit val spark:SparkSession = SparkSession
    .builder()
    .master("local[6]")
    .appName(this.getClass.getSimpleName)
    .getOrCreate()



}
