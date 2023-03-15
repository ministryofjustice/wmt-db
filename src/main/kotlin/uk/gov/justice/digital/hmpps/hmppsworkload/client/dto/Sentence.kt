package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger
import java.time.LocalDate

data class Sentence @JsonCreator constructor(
  val sentenceType: SentenceType,
  val originalLength: BigInteger? = null,
  val originalLengthUnits: String? = null,
  val description: String,
  val startDate: LocalDate,
  val sentenceId: BigInteger,
  val expectedSentenceEndDate: LocalDate? = null,
  val terminationDate: LocalDate? = null,
) {
  fun mapOrder(): String = "$description ($originalLength $originalLengthUnits)"
}

data class SentenceType @JsonCreator constructor(
  val code: String,
  val description: String,
)
