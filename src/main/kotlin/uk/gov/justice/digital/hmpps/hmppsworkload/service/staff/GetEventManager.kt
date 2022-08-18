package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.EventCase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import java.util.UUID

interface GetEventManager {
  fun findById(id: UUID): EventManagerEntity?

  fun findLatestByStaffAndTeam(staffCode: String, teamCode: String): EventCase?
}
