package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ChoosePractitionerResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.CommunityPersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Name
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.PractitionerTeam
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.ProbationStatus
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import uk.gov.justice.digital.hmpps.hmppsworkload.service.calculateCapacity
import java.math.BigDecimal

/***
 * Person on probation and practitioner workload
 */
data class PractitionerWorkload @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val tier: Tier,
  val probationStatus: ProbationStatus,
  val communityPersonManager: CommunityPersonManager,
  val teams: Map<String, List<Practitioner>>
) {
  companion object {
    fun from(
      choosePractitionerResponse: ChoosePractitionerResponse,
      tier: Tier,
      teams: Map<String, List<Practitioner>>
    ): PractitionerWorkload {
      return PractitionerWorkload(
        choosePractitionerResponse.crn,
        choosePractitionerResponse.name,
        tier,
        choosePractitionerResponse.probationStatus,
        choosePractitionerResponse.communityPersonManager,
        teams
      )
    }
  }
}

data class Practitioner constructor(
  val code: String,
  val name: Name,
  val email: String?,
  val grade: String,
  val workload: BigDecimal,
  val casesPastWeek: Int,
  val communityCases: Int,
  val custodyCases: Int
) {
  companion object {
    fun from(practitionerTeam: PractitionerTeam, practitionerWorkload: TeamOverview, caseCount: Int): Practitioner {
      return Practitioner(
        practitionerTeam.code, practitionerTeam.name,
        practitionerTeam.email.takeUnless { email -> email.isNullOrBlank() },
        practitionerTeam.grade ?: "DMY",
        calculateCapacity(practitionerWorkload.totalPoints, practitionerWorkload.availablePoints),
        caseCount,
        practitionerWorkload.totalCommunityCases,
        practitionerWorkload.totalCustodyCases
      )
    }
  }
}
