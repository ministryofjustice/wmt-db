package uk.gov.justice.digital.hmpps.hmppsworkload.domain

import com.fasterxml.jackson.annotation.JsonCreator

data class TeamSummary @JsonCreator constructor(

  val offenderManagers: List<OffenderManagerWorkload>
) {
  companion object {
    fun from(teamOverviews: List<OffenderManagerWorkload>): TeamSummary {
      return TeamSummary(teamOverviews)
    }
  }
}
