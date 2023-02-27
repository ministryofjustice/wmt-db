package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.WorkforceAllocationsToDeliusApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.StaffIdentifier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.GetPersonManager

@Service
class SaveCaseDetailsService(
  @Qualifier("hmppsTierApiClient") private val hmppsTierApiClient: HmppsTierApiClient,
  private val caseDetailsRepository: CaseDetailsRepository,
  private val workloadCalculationService: WorkloadCalculationService,
  private val getPersonManager: GetPersonManager,
  private val updateWorkloadService: UpdateWorkloadService,
  private val workforceAllocationsToDeliusApiClient: WorkforceAllocationsToDeliusApiClient
) {

  fun save(crn: String) {
    val personSummary = workforceAllocationsToDeliusApiClient.getSummaryByCrn(crn).block()!!
    personSummary.takeUnless { it.type == CaseType.UNKNOWN }?.type?.let { caseType ->
      hmppsTierApiClient.getTierByCrn(crn).map {
        val tier = Tier.valueOf(it)
        CaseDetailsEntity(crn, tier, caseType, personSummary.name.forename, personSummary.name.surname)
      }.block()?.let {
        caseDetailsRepository.save(it)
        val staff: PersonManager? = getPersonManager.findLatestByCrn(crn)
        if (staff != null) {
          workloadCalculationService.saveWorkloadCalculation(StaffIdentifier(staff.staffCode, staff.teamCode), staff.staffGrade)
        }
      }
    } ?: caseDetailsRepository.findByIdOrNull(crn)?.let {
      caseDetailsRepository.delete(it)
      updateWorkloadService.setWorkloadInactive(crn)
    }
  }
}
