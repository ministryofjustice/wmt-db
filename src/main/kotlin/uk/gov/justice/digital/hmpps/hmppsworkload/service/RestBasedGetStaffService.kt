package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import java.math.BigInteger

@Service
class RestBasedGetStaffService(private val communityApiClient: CommunityApiClient) : GetStaffService {
  override fun getStaffById(staffId: BigInteger): Staff? =
    communityApiClient.getStaffById(staffId).block()
}
