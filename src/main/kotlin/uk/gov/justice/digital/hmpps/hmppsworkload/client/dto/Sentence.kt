package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger
import java.time.LocalDate

data class Sentence @JsonCreator constructor(
  val sentenceType: SentenceType,
  val originalLength: BigInteger,
  val originalLengthUnits: String,
  val description: String,
  val startDate: LocalDate,
)

data class SentenceType @JsonCreator constructor(
  val code: String,
  val description: String
)
