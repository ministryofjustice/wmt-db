package uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.mockserver.integration.ClientAndServer
import org.mockserver.matchers.Times
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.mockserver.WorkforceAllocationsToDeliusExtension.Companion.workforceAllocationsToDelius
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius.choosePractitionerByTeamCodesNoCommunityPersonManagerResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius.choosePractitionerByTeamResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius.choosePractitionerByTeamResponseUnallocated
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius.choosePractitionerStaffInMultipleTeamsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.workforceAllocationsToDelius.impactResponse

class WorkforceAllocationsToDeliusExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

  companion object {
    lateinit var workforceAllocationsToDelius: WorkforceAllocationsToDeliusMockServer
  }

  override fun beforeAll(context: ExtensionContext?) {
    workforceAllocationsToDelius = WorkforceAllocationsToDeliusMockServer()
  }
  override fun beforeEach(context: ExtensionContext?) {
    workforceAllocationsToDelius.reset()
  }
  override fun afterAll(context: ExtensionContext?) {
    workforceAllocationsToDelius.stop()
  }
}
class WorkforceAllocationsToDeliusMockServer : ClientAndServer(MOCKSERVER_PORT) {

  companion object {
    private const val MOCKSERVER_PORT = 8084
  }

  fun choosePractitionerByTeamCodesResponse(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDelius.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(choosePractitionerByTeamResponse())
    )
  }

  fun choosePractitionerByTeamCodesResponseUnallocated(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDelius.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(choosePractitionerByTeamResponseUnallocated())
    )
  }

  fun choosePractitionerByTeamCodesResponseNoCommunityPersonManager(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDelius.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(
        choosePractitionerByTeamCodesNoCommunityPersonManagerResponse()
      )
    )
  }

  fun choosePractitionerStaffInMultipleTeamsResponse(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDelius.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(choosePractitionerStaffInMultipleTeamsResponse())
    )
  }

  fun getImpactResponse(crn: String, staffCode: String) {
    val impactRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/impact").withQueryStringParameter("crn", crn).withQueryStringParameter("staff", staffCode)

    workforceAllocationsToDelius.`when`(impactRequest, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(impactResponse(crn, staffCode, "PO"))
    )
  }

  fun getImpactNoGradeResponse(crn: String, staffCode: String) {
    val impactRequest =
      HttpRequest.request()
        .withPath("/allocation-demand/impact").withQueryStringParameter("crn", crn).withQueryStringParameter("staff", staffCode)

    workforceAllocationsToDelius.`when`(impactRequest, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(impactResponse(crn, staffCode, null))
    )
  }
}
