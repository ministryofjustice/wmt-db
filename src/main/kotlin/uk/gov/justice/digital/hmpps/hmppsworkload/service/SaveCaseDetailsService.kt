package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.HmppsTierApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.mapper.CaseTypeMapper

@Service
class SaveCaseDetailsService(
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val caseTypeMapper: CaseTypeMapper,
  @Qualifier("hmppsTierApiClient") private val hmppsTierApiClient: HmppsTierApiClient,
  private val caseDetailsRepository: CaseDetailsRepository
) {
  fun save(crn: String) {
    val convictions = communityApiClient.getActiveConvictions(crn).block()
    val caseType = caseTypeMapper.getCaseType(convictions, convictions.first().convictionId)
    val tier = Tier.valueOf(hmppsTierApiClient.getTierByCrn(crn).block())
    val case = CaseDetailsEntity(crn = crn, type = caseType, tier = tier)
    caseDetailsRepository.save(case)
  }

  fun update(crn: String) {
    val convictions = communityApiClient.getActiveConvictions(crn).block()
    val caseType = caseTypeMapper.getCaseType(convictions, convictions.first().convictionId)
    val tier = Tier.valueOf(hmppsTierApiClient.getTierByCrn(crn).block())
    val case = CaseDetailsEntity(crn = crn, type = caseType, tier = tier)

    val foundCase = caseDetailsRepository.findFirstByCrnOrderByCreatedDateDesc(crn).firstOrNull()

    if ((foundCase != null) && ((foundCase.tier != case.tier) || (foundCase.type != case.type))) {
      caseDetailsRepository.save(case)
    } else if (foundCase == null) {
      caseDetailsRepository.save(case)
    }
  }
}
