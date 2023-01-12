package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class ChoosePractitionerResponse @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val probationStatus: ProbationStatus,
  val communityPersonManager: CommunityPersonManager,
  // staff list + enrich with workload data
  val teams: Map<String, List<PractitionerTeam>>
)

data class Name constructor(
  val forename: String,
  val middleName: String?,
  val surname: String
)

data class ProbationStatus constructor(
  val status: String,
  val description: String
)

data class CommunityPersonManager constructor(
  val code: String,
  val name: Name,
  val grade: String?,
  val teamCode: String
)

data class PractitionerTeam constructor(
  val code: String,
  val name: Name,
  val email: String,
  val grade: String?,
)
