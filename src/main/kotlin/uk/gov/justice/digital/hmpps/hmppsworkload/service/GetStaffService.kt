package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import java.math.BigInteger

interface GetStaffService {
  fun getStaffById(staffId: BigInteger): Staff?
}
