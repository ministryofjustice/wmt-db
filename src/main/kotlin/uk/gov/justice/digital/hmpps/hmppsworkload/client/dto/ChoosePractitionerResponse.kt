package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator

/***
 * workforce-allocations-to-delius api response
 */
data class ChoosePractitionerResponse @JsonCreator constructor(
  val crn: String,
  val name: Name,
  val probationStatus: ProbationStatus,
  val communityPersonManager: CommunityPersonManager?,
  val teams: Map<String, List<StaffMember>>
)

data class Name constructor(
  val forename: String,
  val middleName: String?,
  val surname: String
) {
  fun getCombinedName() = "$forename ${middleName?.takeUnless { it.isBlank() }?.let{ "$middleName " } ?: ""}$surname"
}

data class ProbationStatus constructor(
  val status: String,
  val description: String
)

data class CommunityPersonManager constructor(
  val code: String,
  val name: Name,
  val grade: String?,
  val teamCode: String
) {
  val isUnallocated: Boolean = code.endsWith("U")
}

data class StaffMember constructor(
  val code: String,
  val name: Name,
  val email: String?,
  private val grade: String?,
) {
  fun getGrade(): String = grade ?: "DMY"
}
