package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.mockserver.integration.ClientAndServer
import org.mockserver.integration.ClientAndServer.startClientAndServer
import org.mockserver.matchers.Times
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.MediaType
import org.mockserver.model.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByIdResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.teamStaffResponse

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTestBase {

  private var oauthMock: ClientAndServer = startClientAndServer(9090)
  var communityApi: ClientAndServer = startClientAndServer(8092)
  var hmppsTier: ClientAndServer = startClientAndServer(8082)
  private val gson: Gson = Gson()

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  @BeforeEach
  fun `setup dependent services`() {
    communityApi.reset()
    hmppsTier.reset()
    setupOauth()
  }

  @AfterAll
  fun tearDownServer() {
    communityApi.stop()
    oauthMock.stop()
    hmppsTier.stop()
  }

  internal fun HttpHeaders.authToken(roles: List<String> = emptyList()) {
    this.setBearerAuth(
      jwtAuthHelper.createJwt(
        subject = "SOME_USER",
        roles = roles,
        clientId = "some-client"
      )
    )
  }

  fun setupOauth() {
    val response = HttpResponse.response().withContentType(MediaType.APPLICATION_JSON)
      .withBody(gson.toJson(mapOf("access_token" to "ABCDE", "token_type" to "bearer")))
    oauthMock.`when`(HttpRequest.request().withPath("/auth/oauth/token")).respond(response)
  }

  protected fun teamStaffResponse(teamCode: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/teams/$teamCode/staff")

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(teamStaffResponse())
    )
  }

  protected fun tierCalculationResponse(crn: String) {
    val request = HttpRequest.request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody("{\"tierScore\":\"B3\"}")
    )
  }

  protected fun staffIdResponse(staffId: Long, staffCode: String, staffGradeCode: String = "PSM") {
    val request = HttpRequest.request().withPath("/staff/staffIdentifier/$staffId")
    communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(staffByIdResponse(staffCode, staffGradeCode))
    )
  }

  protected fun singleActiveConvictionResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleActiveConvictionResponse())
    )
  }
}
