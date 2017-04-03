package observatory.utils

import java.nio.file.Paths

/**
  * Created by Tom Lous on 24/03/17.
  * Copyright © 2017 Tom Lous
  */
object Resources {

  def resourcePath(resource: String): String =
    Paths.get(getClass.getResource(resource).toURI).toString
}
