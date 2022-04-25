package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger
import java.time.LocalDateTime

data class Conviction @JsonCreator constructor(
  val sentence: Sentence?,
  val custody: Custody?,
  val convictionId: BigInteger,
  val courtAppearance: CourtAppearance?,
  val offences: List<Offence>?
)

data class CourtAppearance @JsonCreator constructor(
  val appearanceDate: LocalDateTime,
  val courtName: String
)

data class Custody @JsonCreator constructor(
  val status: CustodyStatus,
)

data class CustodyStatus @JsonCreator constructor(
  val code: String,
)
