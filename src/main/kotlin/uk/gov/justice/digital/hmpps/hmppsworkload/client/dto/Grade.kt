package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

open class Grade(private val grade: String?) {
  fun getGrade(): String = grade ?: "DMY"
}
