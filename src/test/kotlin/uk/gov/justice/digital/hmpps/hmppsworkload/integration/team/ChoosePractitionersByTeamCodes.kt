package uk.gov.justice.digital.hmpps.hmppsworkload.integration.team

import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PersonManagerEntity
import java.math.BigInteger
import java.time.ZonedDateTime

class ChoosePractitionersByTeamCodes : IntegrationTestBase() {

  @Test
  fun `can get choose practitioner response`() {
    val teamCode = "T1"
    val teamCode2 = "T2"
    val crn = "CRN1"
    choosePractitionerByTeamCodesResponse(listOf(teamCode, teamCode2), crn)
    val firstWmtStaff = setupCurrentWmtStaff("OM1", teamCode)
    val secondWmtStaff = setupCurrentWmtStaff("OM2", teamCode2)

    val firstOm = firstWmtStaff.offenderManager.code
    val secondOm = secondWmtStaff.offenderManager.code
    val noWorkloadStaffCode = "NOWORKLOAD1"

    val storedPersonManager = PersonManagerEntity(crn = "CRN5", staffId = BigInteger.valueOf(123456789L), staffCode = firstOm, teamCode = teamCode, createdBy = "USER1", providerCode = "R1", isActive = true)
    personManagerRepository.save(storedPersonManager)

    val movedPersonManager = PersonManagerEntity(crn = "CRN3", staffId = BigInteger.valueOf(123456789L), staffCode = firstOm, teamCode = teamCode, createdBy = "USER1", providerCode = "R1", createdDate = ZonedDateTime.now().minusDays(5L), isActive = false)
    personManagerRepository.save(movedPersonManager)

    val newPersonManager = PersonManagerEntity(crn = "CRN3", staffId = BigInteger.valueOf(56789321L), staffCode = secondOm, teamCode = teamCode2, createdBy = "USER2", providerCode = "R1", createdDate = ZonedDateTime.now().minusDays(2L), isActive = true)
    personManagerRepository.save(newPersonManager)

    val personManagerWithNoWorkload = PersonManagerEntity(crn = "CRN4", staffId = BigInteger.valueOf(56789321L), staffCode = noWorkloadStaffCode, teamCode = "T1", createdBy = "USER2", providerCode = "R1", createdDate = ZonedDateTime.now().minusDays(2L), isActive = true)
    personManagerRepository.save(personManagerWithNoWorkload)

    webTestClient.get()
      .uri("/team/choose-practitioner?crn=$crn&teamCode=$teamCode,$teamCode2")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.crn")
      .isEqualTo(crn)
      .jsonPath("$.name.forename")
      .isEqualTo("Don")
      .jsonPath("$.name.middleName")
      .isEqualTo("")
      .jsonPath("$.name.surname")
      .isEqualTo("Cole")
      .jsonPath("$.probationStatus.status")
      .isEqualTo("PREVIOUSLY_MANAGED")
      .jsonPath("$.probationStatus.description")
      .isEqualTo("Previously managed")
      .jsonPath("$.communityPersonManager.code")
      .isEqualTo("N03A019")
      .jsonPath("$.communityPersonManager.name.forename")
      .isEqualTo("Derek")
      .jsonPath("$.communityPersonManager.name.surname")
      .isEqualTo("Pint")
      .jsonPath("$.communityPersonManager.teamCode")
      .isEqualTo("N03F01")
      .jsonPath("$.communityPersonManager.grade")
      .isEqualTo("PO")
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].name.forename")
      .isEqualTo("Jane")
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].name.surname")
      .isEqualTo("Doe")
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].email")
      .isEqualTo("j.doe@email.co.uk")
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].grade")
      .isEqualTo("PO")
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].workload")
      .isEqualTo(50.toDouble())
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].casesPastWeek")
      .isEqualTo(1)
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].communityCases")
      .isEqualTo(15)
      .jsonPath("$.teams.$teamCode[?(@.code == '$firstOm')].custodyCases")
      .isEqualTo(20)
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].name.forename")
      .isEqualTo("No")
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].name.surname")
      .isEqualTo("Workload")
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].email")
      .isEqualTo("no.workload.email@co.uk")
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].workload")
      .isEqualTo(0)
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].casesPastWeek")
      .isEqualTo(1)
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].communityCases")
      .isEqualTo(0)
      .jsonPath("$.teams.$teamCode[?(@.code == '$noWorkloadStaffCode')].custodyCases")
      .isEqualTo(0)
      .jsonPath("$.teams.$teamCode2[?(@.code == '$secondOm')].name.forename")
      .isEqualTo("Mark")
      .jsonPath("$.teams.$teamCode2[?(@.code == '$secondOm')].name.surname")
      .isEqualTo("Reese")
      .jsonPath("$.teams.$teamCode2[?(@.code == '$secondOm')].email")
      .doesNotExist()
      .jsonPath("$.teams.$teamCode2[?(@.code == '$secondOm')].grade")
      .isEqualTo("PQiP")
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].name.forename")
      .isEqualTo("Jane")
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].name.surname")
      .isEqualTo("Doe")
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].email")
      .isEqualTo("j.doe@email.co.uk")
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].grade")
      .isEqualTo("PO")
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].workload")
      .isEqualTo(50.toDouble())
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].casesPastWeek")
      .isEqualTo(1)
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].communityCases")
      .isEqualTo(15)
      .jsonPath("$.teams.all[?(@.code == '$firstOm')].custodyCases")
      .isEqualTo(20)
  }

  @Test
  fun `can filter officers by grade`() {
    val teamCode = "T1"
    teamStaffResponse(teamCode)
    val firstWmtStaff = setupCurrentWmtStaff("OM1", teamCode)
    setupCurrentWmtStaff("OM2", teamCode)
    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers?grades=PO,PQiP")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].forename")
      .isEqualTo("Jane")
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].surname")
      .isEqualTo("Doe")
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].grade")
      .isEqualTo("PO")
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].totalCommunityCases")
      .isEqualTo(15)
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].totalCustodyCases")
      .isEqualTo(20)
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].capacity")
      .isEqualTo(50.toDouble())
      .jsonPath("$.offenderManagers[?(@.code == '${firstWmtStaff.offenderManager.code}')].code")
      .isEqualTo("OM1")
      .jsonPath("$.offenderManagers[?(@.code == 'NOWORKLOAD1')]")
      .doesNotExist()
  }

  @Test
  fun `must return not found when team code is not matched`() {
    webTestClient.get()
      .uri("/team/RANDOMCODE/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isNotFound
  }

  @Test
  fun `must return forbidden when auth token does not contain correct role`() {
    webTestClient.get()
      .uri("/team/T1/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_RANDOM_ROLE")) }
      .exchange()
      .expectStatus()
      .isForbidden
  }

  @Test
  fun `can get team overview of offender manager by team code when assigned to another staff member`() {
    val teamCode = "TEAM1"
    teamStaffResponse(teamCode, "STAFF1")

    val movedPersonManager = PersonManagerEntity(crn = "CRN3", staffId = BigInteger.valueOf(123456789L), staffCode = "STAFF1", teamCode = teamCode, createdBy = "USER1", providerCode = "R1", isActive = false)
    personManagerRepository.save(movedPersonManager)

    val newPersonManager = PersonManagerEntity(crn = "CRN3", staffId = BigInteger.valueOf(56789321L), staffCode = "STAFF2", teamCode = "TEAM2", createdBy = "USER2", providerCode = "R1", isActive = true)
    personManagerRepository.save(newPersonManager)

    webTestClient.get()
      .uri("/team/$teamCode/offenderManagers")
      .headers { it.authToken(roles = listOf("ROLE_WORKLOAD_MEASUREMENT")) }
      .exchange()
      .expectStatus()
      .isOk
      .expectBody()
      .jsonPath("$.offenderManagers[?(@.code == 'STAFF1')].totalCasesInLastWeek")
      .isEqualTo(0)
  }
}
