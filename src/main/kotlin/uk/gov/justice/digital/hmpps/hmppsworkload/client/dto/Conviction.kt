package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime

data class Conviction @JsonCreator constructor(
  val sentence: Sentence? = null,
  val custody: Custody? = null,
  val active: Boolean,
  val convictionId: BigInteger,
  val courtAppearance: CourtAppearance? = null,
  val offences: List<Offence>? = null
)

data class CourtAppearance @JsonCreator constructor(
  val appearanceDate: LocalDateTime,
  val courtName: String
)

data class Custody @JsonCreator constructor(
  val status: CustodyStatus,
  val keyDates: CustodyKeyDates?
)

data class CustodyStatus @JsonCreator constructor(
  val code: String,
)

data class CustodyKeyDates @JsonCreator constructor(
  val expectedReleaseDate: LocalDate?
)
