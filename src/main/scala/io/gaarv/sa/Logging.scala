package io.gaarv.sa

import com.typesafe.scalalogging.Logger

trait Logging {

  protected lazy val logger: Logger = Logger("root")

}
