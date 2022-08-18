package uk.gov.justice.digital.hmpps.hmppsworkload.integration.eventManager

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.EventCase
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.CaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.EventManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.service.staff.GetEventManager
import java.math.BigInteger

class GetLatestEventManagerTest : IntegrationTestBase() {

  @Autowired
  private lateinit var getEventManager: GetEventManager

  @Test
  fun `must return latest case allocated when last allocated case requested`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val savedEntity = eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN6634", eventId = BigInteger.TEN, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val eventManagerEntity = eventManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = EventCase(Tier.A1, CaseType.LICENSE, eventManagerEntity.crn, eventManagerEntity.createdDate!!)
    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type))
    val result = getEventManager.findLatestByStaffAndTeam(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }

  @Test
  fun `must only return when case details are known`() {
    val staffCode = "OM1"
    val teamCode = "T1"
    eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN6634", eventId = BigInteger.TEN, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    Assertions.assertNull(getEventManager.findLatestByStaffAndTeam(staffCode, teamCode))
  }

  @Test
  fun `must return latest case when multiple are allocated`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val eventManagerEntity = eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN6634", eventId = BigInteger.TEN, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    caseDetailsRepository.save(CaseDetailsEntity(eventManagerEntity.crn, Tier.A1, CaseType.LICENSE))

    val savedEntity = eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN9977", eventId = BigInteger.TWO, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val latestEventManagerEntity = eventManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = EventCase(Tier.C3, CaseType.COMMUNITY, latestEventManagerEntity.crn, latestEventManagerEntity.createdDate!!)
    // realtime
    caseDetailsRepository.save(CaseDetailsEntity(latestEventManagerEntity.crn, realtimeCase.tier, realtimeCase.type))

    val result = getEventManager.findLatestByStaffAndTeam(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }

  @Test
  fun `must return latest case which has case details`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val savedEntity = eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN6634", eventId = BigInteger.TEN, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val eventManagerEntity = eventManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = EventCase(Tier.A1, CaseType.LICENSE, eventManagerEntity.crn, eventManagerEntity.createdDate!!)
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type))

    eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN9977", eventId = BigInteger.TWO, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    val result = getEventManager.findLatestByStaffAndTeam(staffCode, teamCode)

    Assertions.assertEquals(realtimeCase, result)
  }

  @Test
  fun `must not return event manager if allocated to another staff member`() {
    val staffCode = "OM1"
    val teamCode = "T1"

    val savedEntity = eventManagerRepository.save(
      EventManagerEntity(
        crn = "CRN6634", eventId = BigInteger.TEN, staffCode = staffCode,
        teamCode = teamCode, staffId = BigInteger.TEN, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )
    val eventManagerEntity = eventManagerRepository.findByIdOrNull(savedEntity.id!!)!!
    val realtimeCase = EventCase(Tier.A1, CaseType.LICENSE, eventManagerEntity.crn, eventManagerEntity.createdDate!!)
    caseDetailsRepository.save(CaseDetailsEntity(realtimeCase.crn, realtimeCase.tier, realtimeCase.type))

    eventManagerRepository.save(
      EventManagerEntity(
        crn = eventManagerEntity.crn, eventId = eventManagerEntity.eventId, staffCode = "ADIFFEENTCODE",
        teamCode = teamCode, staffId = BigInteger.ONE, createdBy = "createdBy",
        providerCode = "providerCode"
      )
    )

    Assertions.assertNull(getEventManager.findLatestByStaffAndTeam(staffCode, teamCode))
  }
}
