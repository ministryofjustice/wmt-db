package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.amazonaws.services.sqs.AmazonSQS
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
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsAllocationMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.convictionNoSentenceResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.notFoundTierResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSummaryResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.oneOffenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveRequirementResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveUnpaidRequirementResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleInactiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByCodeResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByIdResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.teamStaffResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.HmppsOffenderEvent
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigInteger

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

  @Autowired
  protected lateinit var eventManagerRepository: EventManagerRepository

  @Autowired
  protected lateinit var caseDetailsRepository: CaseDetailsRepository

  @Autowired
  protected lateinit var requirementManagerRepository: RequirementManagerRepository

  @Autowired
  protected lateinit var sentenceRepository: SentenceRepository

  @Qualifier("hmppsallocationcompletequeue-sqs-client")
  @Autowired
  lateinit var allocationCompleteClient: AmazonSQSAsync

  protected val allocationCompleteUrl by lazy { hmppsQueueService.findByQueueId("hmppsallocationcompletequeue")?.queueUrl ?: throw MissingQueueException("HmppsQueue allocationcompletequeue not found") }

  private val hmppsOffenderQueue by lazy { hmppsQueueService.findByQueueId("hmppsoffenderqueue") ?: throw MissingQueueException("HmppsQueue hmppsoffenderqueue not found") }
  private val hmppsOffenderTopic by lazy { hmppsQueueService.findByTopicId("hmppsoffendertopic") ?: throw MissingQueueException("HmppsTopic hmppsoffendertopic not found") }

  private val hmppsOffenderSqsDlqClient by lazy { hmppsOffenderQueue.sqsDlqClient as AmazonSQS }
  protected val hmppsOffenderSqsClient by lazy { hmppsOffenderQueue.sqsClient }

  protected val hmppsOffenderSnsClient by lazy { hmppsOffenderTopic.snsClient }
  protected val hmppsOffenderTopicArn by lazy { hmppsOffenderTopic.arn }

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  @BeforeEach
  fun setupDependentServices() {
    communityApi.reset()
    hmppsTier.reset()
    offenderSearchApi.reset()
    setupOauth()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
    caseDetailsRepository.deleteAll()
    allocationCompleteClient.purgeQueue(PurgeQueueRequest(allocationCompleteUrl))
    hmppsOffenderSqsClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.queueUrl))
    hmppsOffenderSqsDlqClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.dlqUrl))
  }

  @AfterAll
  fun tearDownServer() {
    communityApi.stop()
    oauthMock.stop()
    hmppsTier.stop()
    offenderSearchApi.stop()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
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

  protected fun countMessagesOnOffenderEventQueue(): Int =
    hmppsOffenderSqsClient.getQueueAttributes(hmppsOffenderQueue.queueUrl, listOf("ApproximateNumberOfMessages", "ApproximateNumberOfMessagesNotVisible"))
      .let { (it.attributes["ApproximateNumberOfMessages"]?.toInt() ?: 0) + (it.attributes["ApproximateNumberOfMessagesNotVisible"]?.toInt() ?: 0) }

  protected fun countMessagesOnOffenderEventDeadLetterQueue(): Int =
    hmppsOffenderSqsDlqClient.getQueueAttributes(hmppsOffenderQueue.dlqUrl, listOf("ApproximateNumberOfMessages"))
      .let { it.attributes["ApproximateNumberOfMessages"]?.toInt() ?: 0 }

  protected fun offenderEvent(crn: String, sentenceId: BigInteger) = HmppsOffenderEvent(crn, sentenceId)

  protected fun jsonString(any: Any) = objectMapper.writeValueAsString(any) as String

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

  protected fun tierCalculationResponse(crn: String, tier: String = "B3") {
    val request = HttpRequest.request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody("{\"tierScore\":\"${tier}\"}")
    )
  }

  protected fun tierCalculationNotFoundResponse(crn: String) {
    val request = HttpRequest.request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request, Times.exactly(1)).respond(
      HttpResponse.notFoundResponse().withContentType(MediaType.APPLICATION_JSON).withBody(notFoundTierResponse())
    )
  }

  protected fun staffIdResponse(staffId: BigInteger, staffCode: String, teamCode: String, staffGradeCode: String = "PSM") {
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

  protected fun convictionWithNoSentenceResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(convictionNoSentenceResponse())
    )
  }

  protected fun noConvictionsResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody("[]")
    )
  }

  protected fun singleActiveConvictionResponseForAllConvictions(crn: String) {
    val convictionsRequest =
      HttpRequest.request()
        .withPath("/offenders/crn/$crn/convictions")

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleActiveConvictionResponse())
    )
  }

  protected fun singleInactiveConvictionsResponse(crn: String) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions")
    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleInactiveConvictionResponse())
    )
  }

  protected fun offenderSummaryResponse(crn: String) {
    val summaryRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn")

    communityApi.`when`(summaryRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(offenderSummaryResponse())
    )
  }

  protected fun expectWorkloadAllocationCompleteMessages(crn: String) {
    numberOfMessagesCurrentlyOnQueue(allocationCompleteClient, allocationCompleteUrl, 3)
    val changeEvents = getAllAllocationMessages()
    changeEvents.forEach { changeEvent ->
      Assertions.assertEquals(crn, changeEvent.personReference.identifiers.first { it.type == "CRN" }.value)
    }
    val numberOfPersonAllocationMessages = changeEvents.count { it.eventType == "person.community.manager.allocated" }
    Assertions.assertEquals(1, numberOfPersonAllocationMessages)

    val numberOfEventAllocationMessages = changeEvents.count { it.eventType == "event.manager.allocated" }
    Assertions.assertEquals(1, numberOfEventAllocationMessages)

    val numberOfRequirementAllocationMessages = changeEvents.count { it.eventType == "requirement.manager.allocated" }
    Assertions.assertEquals(1, numberOfRequirementAllocationMessages)
  }

  private fun getAllAllocationMessages(): List<HmppsMessage<HmppsAllocationMessage>> {
    val messages = ArrayList<HmppsMessage<HmppsAllocationMessage>>()
    while (getNumberOfMessagesCurrentlyOnQueue(allocationCompleteClient, allocationCompleteUrl) != 0) {
      val message = allocationCompleteClient.receiveMessage(allocationCompleteUrl)
      messages.addAll(
        message.messages.map {
          val sqsMessage = objectMapper.readValue(it.body, SQSMessage::class.java)
          val personAllocationMessageType = object : TypeReference<HmppsMessage<HmppsAllocationMessage>>() {}
          objectMapper.readValue(sqsMessage.Message, personAllocationMessageType)
        }
      )
    }
    return messages
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

  protected fun singleActiveUnpaidRequirementResponse(crn: String, convictionId: BigInteger) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleActiveUnpaidRequirementResponse())
    )
  }

  protected fun singleActiveRequirementResponse(crn: String, convictionId: BigInteger, requirementId: BigInteger = BigInteger.valueOf(123456789L)) {
    val convictionsRequest =
      HttpRequest.request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      HttpResponse.response().withContentType(MediaType.APPLICATION_JSON).withBody(singleActiveRequirementResponse(requirementId))
    )
  }
}

data class SQSMessage(
  val Message: String,
  val MessageId: String
)
