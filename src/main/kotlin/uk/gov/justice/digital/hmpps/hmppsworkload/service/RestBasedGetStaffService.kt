package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffSummary
import java.math.BigInteger

@Service
class RestBasedGetStaffService(@Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient) : GetStaffService {
  override fun getStaffById(staffId: BigInteger): Staff? =
    communityApiClient.getStaffById(staffId).block()

  override fun getStaffByCode(staffCode: String): StaffSummary? = communityApiClient.getStaffByCode(staffCode).block()
}
