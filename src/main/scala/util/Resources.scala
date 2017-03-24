package util

import java.nio.file.Paths

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Datlinq B.V..
  */
object Resources {

  def resourcePath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString
}
