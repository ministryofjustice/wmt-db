package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.PersonManager
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.CaseTypeMapper
import javax.transaction.Transactional

@Service
class SaveCaseDetailsService(
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val caseTypeMapper: CaseTypeMapper,
  @Qualifier("hmppsTierApiClient") private val hmppsTierApiClient: HmppsTierApiClient,
  private val caseDetailsRepository: CaseDetailsRepository,
  private val workloadCalculationService: WorkloadCalculationService,
  @Qualifier("offenderManagerService") private val offenderManagerService: OffenderManagerService

) {
  @Transactional
  fun save(crn: String) {
    val convictions = communityApiClient.getActiveConvictions(crn).block()!!
    caseTypeMapper.getCaseType(convictions).takeUnless { it == CaseType.UNKNOWN }?.let { caseType ->
      hmppsTierApiClient.getTierByCrn(crn).map {
        val tier = Tier.valueOf(it)
        val case =
          caseDetailsRepository.findByIdOrNull(crn) ?: CaseDetailsEntity(crn, tier, caseType)
        case.type = caseType
        case.tier = tier
        case
      }.block()?.let {
        caseDetailsRepository.save(it)
        val staff: PersonManager? = offenderManagerService.getByCrn(crn)
        if (staff != null) {
          workloadCalculationService.calculate(staff.staffCode, staff.teamCode, staff.providerCode, staff.staffGrade)
        }
      }
    } ?: caseDetailsRepository.findByIdOrNull(crn)?.let { caseDetailsRepository.delete(it) }
  }
}
