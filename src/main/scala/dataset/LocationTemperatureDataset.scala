package dataset

import dataset.StationDataset.StationDataset
import dataset.TemperatureDataset.TemperatureDataset
import model.{LocationTemperatureRecord, Station}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.types.DoubleType

/**
  * Created by Tom Lous on 25/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
object LocationTemperatureDataset {

  type LocationTemperatureDataset = Dataset[LocationTemperatureRecord]

  def apply(stationsDataset: StationDataset, temperatureDataset: TemperatureDataset)(implicit spark: SparkSession): LocationTemperatureDataset = {
    import spark.implicits._

    stationsDataset
      .join(temperatureDataset, usingColumn = "composedId")
      .where('location.isNotNull)
      .where('localDate.isNotNull)
      .where('celcius.isNotNull)
      .select('location, 'localDate, 'celsius)
    .as[LocationTemperatureRecord]



  }
}
