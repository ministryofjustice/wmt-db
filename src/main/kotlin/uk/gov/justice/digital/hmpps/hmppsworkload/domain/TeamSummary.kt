package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerOverview

data class TeamSummary @JsonCreator constructor(

  val offenderManagers: List<OffenderManagerWorkload>
) {
  companion object {
    fun from(teamOverviews: List<OffenderManagerOverview>): TeamSummary {
      return TeamSummary(teamOverviews.map { OffenderManagerWorkload.from(it) })
    }
  }
}
