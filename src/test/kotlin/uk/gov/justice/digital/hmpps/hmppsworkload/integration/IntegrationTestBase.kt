package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
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
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsPersonAllocationMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSummaryResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.oneOffenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByCodeResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByIdResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.teamStaffResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTestBase {

  private var oauthMock: ClientAndServer = startClientAndServer(9090)
  var communityApi: ClientAndServer = startClientAndServer(8092)
  var hmppsTier: ClientAndServer = startClientAndServer(8082)
  var offenderSearchApi: ClientAndServer = startClientAndServer(8095)

  @Autowired
  protected lateinit var objectMapper: ObjectMapper

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  @Autowired
  protected lateinit var personManagerRepository: PersonManagerRepository

  @Qualifier("hmppsallocationcompletequeue-sqs-client")
  @Autowired
  lateinit var allocationCompleteClient: AmazonSQSAsync

  protected val allocationCompleteUrl by lazy { hmppsQueueService.findByQueueId("hmppsallocationcompletequeue")?.queueUrl ?: throw MissingQueueException("HmppsQueue allocationcompletequeue not found") }

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  @BeforeEach
  fun `setup dependent services`() {
    communityApi.reset()
    hmppsTier.reset()
    offenderSearchApi.reset()
    setupOauth()
    personManagerRepository.deleteAll()
    allocationCompleteClient.purgeQueue(PurgeQueueRequest(allocationCompleteUrl))
  }

  @AfterAll
  fun tearDownServer() {
    communityApi.stop()
    oauthMock.stop()
    hmppsTier.stop()
    offenderSearchApi.stop()
    personManagerRepository.deleteAll()
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
      .withBody(objectMapper.writeValueAsString(mapOf("access_token" to "ABCDE", "token_type" to "bearer")))
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

  protected fun staffIdResponse(staffId: Long, staffCode: String, teamCode: String, staffGradeCode: String = "PSM") {
    val request = HttpRequest.request().withPath("/staff/staffIdentifier/$staffId")
    communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(staffByIdResponse(staffCode, staffGradeCode, teamCode))
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

  protected fun offenderSummaryResponse(crn: String) {
    val summaryRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn")

    communityApi.`when`(summaryRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(offenderSummaryResponse())
    )
  }

  protected fun expectPersonAllocationCompleteMessage(crn: String) {
    oneMessageCurrentlyOnQueue(allocationCompleteClient, allocationCompleteUrl)
    val message = allocationCompleteClient.receiveMessage(allocationCompleteUrl)

    val sqsMessage = objectMapper.readValue(message.messages[0].body, SQSMessage::class.java)
    val personAllocationMessageType = object : TypeReference<HmppsMessage<HmppsPersonAllocationMessage>>() {}

    val changeEvent = objectMapper.readValue(sqsMessage.Message, personAllocationMessageType)
    Assertions.assertEquals(crn, changeEvent.personReference.identifiers.first { it.type == "CRN" }.value)
  }

  protected fun staffCodeResponse(staffCode: String, teamCode: String) {
    val request = HttpRequest.request().withPath("/staff/staffCode/$staffCode")
    communityApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(staffByCodeResponse(staffCode, teamCode))
    )
  }

  protected fun offenderSearchByCrnsResponse(crns: List<String>) {
    val request = HttpRequest.request().withPath("/crns").withBody(objectMapper.writeValueAsString(crns))
    offenderSearchApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(offenderSearchByCrnsResponse())
    )
  }

  protected fun oneOffenderReturnedWhenSearchByCrnsResponse(crns: List<String>) {
    val request = HttpRequest.request().withPath("/crns").withBody(objectMapper.writeValueAsString(crns))
    offenderSearchApi.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(oneOffenderSearchByCrnsResponse())
    )
  }
}

data class SQSMessage(
  val Message: String,
  val MessageId: String
)
