package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class ImpactCase @JsonCreator constructor(
  val crn: String,
  val convictionId: BigInteger
)
