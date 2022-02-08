package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.TeamOverview
import java.math.BigDecimal

data class OffenderManagerWorkload @JsonCreator constructor(
  val forename: String,
  val surname: String,
  val grade: String,
  val totalCommunityCases: BigDecimal,
  val totalCustodyCases: BigDecimal,
  val capacity: BigDecimal
) {
  companion object {
    fun from(teamOverview: TeamOverview): OffenderManagerWorkload {
      return OffenderManagerWorkload(
        teamOverview.forename,
        teamOverview.surname,
        teamOverview.grade,
        teamOverview.totalCommunityCases,
        teamOverview.totalCustodyCases,
        BigDecimal(teamOverview.totalPoints)
          .divide(BigDecimal(teamOverview.availablePoints))
      )
    }
  }
}
