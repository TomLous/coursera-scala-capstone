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
/*
import com.datlinq.spark.util.Implicits._

type BagDataset = Dataset[BagEntry]

def toNevenadres = toOptionBoolean("t", "f")

def apply(inputPath: String, dateStr: Option[String] = None)(implicit spark: SparkSession): BagDataset = {

  val sourceFileName = FilenameUtils.getName(inputPath)

  val sourceFileVersion = "2015-11-08" //http://data.nlextract.nl/bag/csv/README

  val sourceFileTimestamp = if (dateStr.isDefined) {
  new SimpleDateFormat("yyyy-MM-dd").parse(dateStr.get).getTime / 1000 + (12 * 60 * 60)
} else {
  val pattern = """\w+\-(\d{4}_\d{2}_\d{2}).csv""".r
  val pattern(dateString) = sourceFileName

  new SimpleDateFormat("yyyy_MM_dd").parse(dateString).getTime / 1000 + (12 * 60 * 60)
}

  import spark.implicits._

  val dataset = spark
  .read

  .option("header", true)
  .option("sep", ";")
  .option("ignoreLeadingWhiteSpace", true)
  .option("ignoreTrailingWhiteSpace", true)
  .option("quote", "")
  .option("nullValue", "")
  .option("mode", "FAILFAST") // - PERMISSIVE : sets other fields to null when it meets a corrupted record. When a schema is set by user, it sets null for extra fields. / - DROPMALFORMED : ignores the whole corrupted records. / - FAILFAST : throws an exception when it meets corrupted records.

  .csv(inputPath)

  .disallowNull("openbareruimte", "postcode", "woonplaats", "gemeente", "provincie", "object_id", "x", "y", "lon", "lat")

  .withColumn("huisnummer", toOptionInt('huisnummer))
  .withColumn("huisletter", toOptionString('huisletter))
  .withColumn("huisnummertoevoeging", toOptionString('huisnummertoevoeging))
  .withColumn("object_id", 'object_id.cast("long"))
  .withColumn("object_type", toOptionString('object_type))
  .withColumn("nevenadres", toNevenadres('nevenadres))
  .withColumn("x", 'x.cast("long"))
  .withColumn("y", 'y.cast("long"))
  .withColumn("lat", 'lat.cast("double"))
  .withColumn("lon", 'lon.cast("double"))

  .as[BagEntry]
  .chain(MetadataTransformer(BagEntry.typename, sourceFileName, sourceFileTimestamp, sourceFileVersion, Some(lit(sourceFileTimestamp))))
  .chain(IDTransformer(md5(concat_ws("-", 'openbareruimte, 'huisnummer, 'huisletter, 'huisnummertoevoeging, 'object_id, 'x, 'y)), None, BagEntry.typename))
  .chain(GeoTransformer($$"lat", $$"lon"))
  .chain(AddressTransformer(
  street = $$"openbareruimte",
  houseNumber = $$"huisnummer",
  houseLetter = $$"huisletter",
  houseNumberAddition = $$"huisnummertoevoeging",
  postalCode = $$"postcode",
  locality = $$"woonplaats",
  administrativeAreaLevel3 = $$"woonplaats",
  administrativeAreaLevel2 = $$"gemeente",
  administrativeAreaLevel1 = $$"provincie",
  country = Some(lit("Nederland")),
  countryCode = Some(lit("NL"))
  ))

  dataset
}*/