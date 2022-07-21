package uk.gov.justice.digital.hmpps.hmppsworkload.utils

fun String.capitalize(): String {
  if (this.isNotEmpty() && this.isNotBlank()) {
    return this[0].uppercase() + this.substring(1).lowercase()
  }
  return this
}
