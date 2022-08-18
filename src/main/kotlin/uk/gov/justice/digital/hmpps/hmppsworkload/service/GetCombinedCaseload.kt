package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Case
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.mapping.OffenderManagerCase
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
    val wmtCases: List<OffenderManagerCase> =
      offenderManagerRepository.findCasesByTeamCodeAndStaffCode(staffCode, teamCode)

    val realtimeCases: List<PersonManagerEntity> =
      personManagerRepository.findByStaffCodeAndTeamCodeLatest(staffCode, teamCode)

    return caseDetailsRepository.findAllById(
      wmtCases
        .map { c -> c.crn }
        .union(realtimeCases.map { c -> c.crn })
    )
      .map { cde -> Case(cde.tier, cde.type, false, cde.crn) }
  }

  override fun getLastAllocatedCase(staffCode: String, teamCode: String): Case? = personManagerRepository.findByStaffCodeAndTeamCodeLatest(staffCode, teamCode)
    .filter { caseDetailsRepository.existsById(it.crn) }
    .maxByOrNull { it.createdDate!! }?.let { personManagerEntity ->
      caseDetailsRepository.findByIdOrNull(personManagerEntity.crn)?.let { caseDetails ->
        Case(caseDetails.tier, caseDetails.type, false, caseDetails.crn, personManagerEntity.createdDate)
      }
    }
}
