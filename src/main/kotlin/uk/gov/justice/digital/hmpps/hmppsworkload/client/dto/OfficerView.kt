package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class OfficerView @JsonCreator constructor(
  val code: String,
  val name: Name,
  val grade: String,
  val email: String?,
  val casesDueToEndInNext4Weeks: BigInteger,
  val releasesWithinNext4Weeks: BigInteger,
  val paroleReportsToCompleteInNext4Weeks: BigInteger
)
