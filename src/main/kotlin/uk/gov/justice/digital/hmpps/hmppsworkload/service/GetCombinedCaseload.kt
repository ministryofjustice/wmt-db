package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository

@Service
class GetCombinedCaseload(
  private val offenderManagerRepository: OffenderManagerRepository,
  private val personManagerRepository: PersonManagerRepository,
  private val caseDetailsRepository: CaseDetailsRepository
) : GetCaseload {

  override fun getCases(staffCode: String, teamCode: String): List<Case> {
    val wmtCases =
      offenderManagerRepository.findCasesByTeamCodeAndStaffCode(staffCode, teamCode)

    val realtimeCases =
      personManagerRepository.findByStaffCodeAndTeamCodeAndIsActiveIsTrue(staffCode, teamCode)

    return caseDetailsRepository.findAllById(
      wmtCases
        .map { c -> c.crn }
        .union(realtimeCases.map { c -> c.crn })
    )
      .map { cde -> Case(cde.tier, cde.type, false, cde.crn) }
  }
}
