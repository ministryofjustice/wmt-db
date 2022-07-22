package uk.gov.justice.digital.hmpps.hmppsworkload.service

import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Staff
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.StaffSummary
import java.math.BigInteger

interface GetStaffService {
  fun getStaffById(staffId: BigInteger): Staff?

  fun getStaffByCode(staffCode: String): StaffSummary?
}
