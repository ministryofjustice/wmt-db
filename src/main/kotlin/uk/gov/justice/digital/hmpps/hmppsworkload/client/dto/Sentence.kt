package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class Sentence @JsonCreator constructor(
  val sentenceType: SentenceType
)

data class SentenceType @JsonCreator constructor(
  val code: String,
)
