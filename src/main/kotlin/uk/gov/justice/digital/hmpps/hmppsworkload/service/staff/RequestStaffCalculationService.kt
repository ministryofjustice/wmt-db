package uk.gov.justice.digital.hmpps.hmppsworkload.service.staff

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.CommunityApiClient
import uk.gov.justice.digital.hmpps.hmppsworkload.service.GetWeeklyHours
import uk.gov.justice.digital.hmpps.hmppsworkload.service.SuccessUpdater
import uk.gov.justice.digital.hmpps.hmppsworkload.service.reduction.GetReductionService

@Service
class RequestStaffCalculationService(
  private val weeklyHours: GetWeeklyHours,
  private val getReductionService: GetReductionService,
  @Qualifier("communityApiClient") private val communityApiClient: CommunityApiClient,
  private val successUpdater: SuccessUpdater
) {

  fun requestStaffCalculation(staffCode: String, teamCode: String) {
    val staffGrade = communityApiClient.getStaffSummaryByCode(staffCode).block()!!.grade
    val weeklyHours = weeklyHours.findWeeklyHours(staffCode, teamCode, staffGrade)
    val reductions = getReductionService.findReductionHours(staffCode, teamCode)
    val availableHours = weeklyHours - reductions
    successUpdater.staffAvailableHoursChange(staffCode, teamCode, availableHours)
  }
}
