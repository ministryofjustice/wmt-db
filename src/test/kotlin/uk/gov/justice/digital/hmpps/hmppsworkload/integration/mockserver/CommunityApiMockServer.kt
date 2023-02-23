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
import org.mockserver.model.Parameter
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.convictionNoSentenceResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.nomsLookupResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleInactiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByCodeResponse

class CommunityApiExtension : BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

  companion object {
    lateinit var communityApi: CommunityApiMockServer
  }

  override fun beforeAll(context: ExtensionContext?) {
    communityApi = CommunityApiMockServer()
  }

  override fun beforeEach(context: ExtensionContext?) {
    communityApi.reset()
  }
  override fun afterAll(context: ExtensionContext?) {
    communityApi.stop()
  }
}
class CommunityApiMockServer : ClientAndServer(MOCKSERVER_PORT) {

  companion object {
    private const val MOCKSERVER_PORT = 8092
  }

  fun singleActiveConvictionResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    CommunityApiExtension.communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveConvictionResponse())
    )
  }

  fun convictionWithNoSentenceResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    CommunityApiExtension.communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(convictionNoSentenceResponse())
    )
  }

  fun noConvictionsResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    CommunityApiExtension.communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody("[]")
    )
  }

  fun singleInactiveConvictionsResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions")
    CommunityApiExtension.communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleInactiveConvictionResponse())
    )
  }

  fun staffCodeResponse(staffCode: String, teamCode: String, staffGrade: String = "PSM", email: String? = "sheila.hancock@test.justice.gov.uk") {
    val request = HttpRequest.request().withPath("/staff/staffCode/$staffCode")
    CommunityApiExtension.communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response()
        .withContentType(MediaType.APPLICATION_JSON).withBody(staffByCodeResponse(staffCode, teamCode, staffGrade, email))
    )
  }

  fun nomsLookupRespond(crn: String, nomsNumber: String) {
    val request = HttpRequest.request().withPath("/secure/offenders/nomsNumber/$nomsNumber")
    CommunityApiExtension.communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(nomsLookupResponse(crn, nomsNumber))
    )
  }

  fun nomsLookupNotFoundRespond(nomsNumber: String) {
    val request = HttpRequest.request().withPath("/secure/offenders/nomsNumber/$nomsNumber")
    CommunityApiExtension.communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withStatusCode(404)
    )
  }
}
