package uk.gov.justice.digital.hmpps.hmppsworkload.utils

import java.time.format.DateTimeFormatter

object DateUtils {
  @JvmStatic
  val notifyDateFormat = DateTimeFormatter.ofPattern("d MMMM yyyy")
}
