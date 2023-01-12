package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.CommunityPersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Name
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ProbationStatus
import java.math.BigDecimal

data class PractitionerWorkload @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val probationStatus: ProbationStatus,
  val communityPersonManager: CommunityPersonManager,
  // staff list + enrich with workload data
  val teams: Map<String, List<Practitioner>>
)

data class Practitioner constructor(
  val code: String,
  val name: Name,
  val email: String?,
  val grade: String,
  val workload: BigDecimal,
  val casesPastWeek: Int,
  val communityCases: BigDecimal,
  val custodyCases: BigDecimal
)
