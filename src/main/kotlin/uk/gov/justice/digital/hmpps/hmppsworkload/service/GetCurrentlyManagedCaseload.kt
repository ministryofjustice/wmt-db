package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository

class GetCurrentlyManagedCaseload(
  private val personManagerRepository: PersonManagerRepository,
  private val caseDetailsRepository: CaseDetailsRepository
) : GetCaseload {
  override fun getCases(staffCode: String, teamCode: String): List<Case> {

    val realtimeCases: List<PersonManagerEntity> =
      personManagerRepository.findByStaffCodeAndTeamCodeLatest(staffCode, teamCode)

    return caseDetailsRepository.findAllById(
      realtimeCases.map { c -> c.crn }
    )
      .map { cde -> Case(cde.tier, cde.type, false, cde.crn) }
  }
}
