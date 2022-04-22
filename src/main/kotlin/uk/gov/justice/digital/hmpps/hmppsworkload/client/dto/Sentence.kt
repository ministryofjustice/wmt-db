package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class Sentence @JsonCreator constructor(
  val sentenceType: SentenceType,
  val originalLength: BigInteger,
  val originalLengthUnits: String,
  val description: String
)

data class SentenceType @JsonCreator constructor(
  val code: String,
  val description: String
)
