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
import org.mockserver.configuration.Configuration
import org.mockserver.integration.ClientAndServer
import org.mockserver.integration.ClientAndServer.startClientAndServer
import org.mockserver.matchers.Times
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.HttpStatusCode
import org.mockserver.model.MediaType.APPLICATION_JSON
import org.mockserver.model.Parameter
import org.slf4j.event.Level
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsAllocationMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.bankHolidayJsonResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.communityApiAssessmentResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.convictionNoSentenceResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.notFoundTierResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSummaryResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.oneOffenderSearchByCrnsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveInductionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveRequirementNoLengthResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveRequirementResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleActiveUnpaidRequirementResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.singleInactiveConvictionResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByCodeResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.staffByUserNameResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.successfulRiskPredictorResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.successfulRiskSummaryResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.teamStaffResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.AdjustmentReasonRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTAssessmentRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCMSRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCourtReportsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTInstitutionalReportRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTWorkloadOwnerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadCalculationRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.HmppsOffenderEvent
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigInteger

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTestBase {

  private var oauthMock: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 9090)
  var communityApi: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8092)
  var hmppsTier: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8082)
  var offenderSearchApi: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8095)
  var bankHolidayApi: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8093)
  var assessRisksNeedsApi: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8085)

  init {
    bankHolidayResponse()
  }

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

  @Autowired
  protected lateinit var wmtCourtReportsRepository: WMTCourtReportsRepository

  @Autowired
  protected lateinit var wmtAssessmentRepository: WMTAssessmentRepository

  @Autowired
  protected lateinit var wmtInstitutionalReportRepository: WMTInstitutionalReportRepository

  @Autowired
  protected lateinit var wmtcmsRepository: WMTCMSRepository

  @Autowired
  protected lateinit var reductionsRepository: ReductionsRepository

  @Autowired
  protected lateinit var wmtWorkloadOwnerRepository: WMTWorkloadOwnerRepository

  @Autowired
  protected lateinit var teamRepository: TeamRepository

  @Autowired
  protected lateinit var offenderManagerRepository: OffenderManagerRepository

  @Autowired
  protected lateinit var adjustmentReasonRepository: AdjustmentReasonRepository

  @Autowired
  protected lateinit var workloadCalculationRepository: WorkloadCalculationRepository

  @Qualifier("hmppsallocationcompletequeue-sqs-client")
  @Autowired
  lateinit var allocationCompleteClient: AmazonSQSAsync

  protected val allocationCompleteUrl by lazy { hmppsQueueService.findByQueueId("hmppsallocationcompletequeue")?.queueUrl ?: throw MissingQueueException("HmppsQueue allocationcompletequeue not found") }

  private val hmppsOffenderQueue by lazy { hmppsQueueService.findByQueueId("hmppsoffenderqueue") ?: throw MissingQueueException("HmppsQueue hmppsoffenderqueue not found") }
  private val domainEventsTopic by lazy { hmppsQueueService.findByTopicId("hmmppsdomaintopic") ?: throw MissingQueueException("HmppsTopic hmmppsdomaintopic not found") }
  private val offenderEventTopic by lazy { hmppsQueueService.findByTopicId("hmppsoffendertopic") ?: throw MissingQueueException("HmppsTopic hmppsoffendertopic not found") }

  private val hmppsDomainQueue by lazy { hmppsQueueService.findByQueueId("tiercalcqueue") ?: throw MissingQueueException("HmppsQueue tiercalcqueue not found") }

  private val hmppsOffenderSqsDlqClient by lazy { hmppsOffenderQueue.sqsDlqClient as AmazonSQS }
  protected val hmppsOffenderSqsClient by lazy { hmppsOffenderQueue.sqsClient }

  protected val hmppsOffenderSnsClient by lazy { offenderEventTopic.snsClient }
  protected val hmppsOffenderTopicArn by lazy { offenderEventTopic.arn }

  protected val hmppsDomainSnsClient by lazy { domainEventsTopic.snsClient }
  protected val hmppsDomainTopicArn by lazy { domainEventsTopic.arn }

  private val hmppsDomainSqsDlqClient by lazy { hmppsDomainQueue.sqsDlqClient as AmazonSQS }
  protected val hmppsDomainSqsClient by lazy { hmppsDomainQueue.sqsClient }

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  @BeforeEach
  fun setupDependentServices() {

    communityApi.reset()
    hmppsTier.reset()
    offenderSearchApi.reset()
    bankHolidayApi.reset()
    assessRisksNeedsApi.reset()
    setupOauth()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
    caseDetailsRepository.deleteAll()
    wmtCourtReportsRepository.deleteAll()
    wmtAssessmentRepository.deleteAll()
    wmtInstitutionalReportRepository.deleteAll()
    wmtcmsRepository.deleteAll()
    reductionsRepository.deleteAll()
    adjustmentReasonRepository.deleteAll()
    allocationCompleteClient.purgeQueue(PurgeQueueRequest(allocationCompleteUrl))
    hmppsOffenderSqsClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.queueUrl))
    hmppsOffenderSqsDlqClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.dlqUrl))

    hmppsDomainSqsClient.purgeQueue(PurgeQueueRequest(hmppsDomainQueue.queueUrl))
    hmppsDomainSqsDlqClient.purgeQueue(PurgeQueueRequest(hmppsDomainQueue.dlqUrl))
    workloadCalculationRepository.deleteAll()
  }

  @AfterAll
  fun tearDownServer() {
    communityApi.stop()
    oauthMock.stop()
    hmppsTier.stop()
    bankHolidayApi.stop()
    offenderSearchApi.stop()
    assessRisksNeedsApi.stop()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
    wmtCourtReportsRepository.deleteAll()
    wmtAssessmentRepository.deleteAll()
    wmtcmsRepository.deleteAll()
    reductionsRepository.deleteAll()
    adjustmentReasonRepository.deleteAll()
    workloadCalculationRepository.deleteAll()
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

  private final fun bankHolidayResponse() {
    val bankHolidayRequest = request()
      .withPath("/bank-holidays.json")

    bankHolidayApi.`when`(bankHolidayRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(bankHolidayJsonResponse())
    )
  }

  fun setupOauth() {
    val response = response().withContentType(APPLICATION_JSON)
      .withBody(objectMapper.writeValueAsString(mapOf("access_token" to "ABCDE", "token_type" to "bearer")))
    oauthMock.`when`(request().withPath("/auth/oauth/token")).respond(response)
  }

  protected fun teamStaffResponse(teamCode: String) {
    val convictionsRequest =
      request()
        .withPath("/teams/$teamCode/staff")

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(teamStaffResponse())
    )
  }

  protected fun tierCalculationResponse(crn: String, tier: String = "B3") {
    val request = request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody("{\"tierScore\":\"${tier}\"}")
    )
  }

  protected fun riskSummaryResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/summary")
    assessRisksNeedsApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(successfulRiskSummaryResponse())
    )
  }

  protected fun riskPredictorResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/predictors/rsr/history")
    assessRisksNeedsApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(successfulRiskPredictorResponse())
    )
  }

  protected fun assessmentCommunityApiResponse(crn: String) {
    val ogrsRequest =
      request().withPath("/offenders/crn/$crn/assessments")

    communityApi.`when`(ogrsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(communityApiAssessmentResponse())
    )
  }
  protected fun riskSummaryErrorResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/summary")
    assessRisksNeedsApi.`when`(request, Times.exactly(1)).respond(
      response().withStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR_500.code()).withContentType(
        APPLICATION_JSON
      ).withBody("{}")
    )
  }

  protected fun tierCalculationNotFoundResponse(crn: String) {
    val request = request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request, Times.exactly(1)).respond(
      HttpResponse.notFoundResponse().withContentType(APPLICATION_JSON).withBody(notFoundTierResponse())
    )
  }

  protected fun singleActiveInductionResponse(crn: String) {
    val inductionRequest =
      request().withPath("/offenders/crn/$crn/contact-summary/inductions")

    communityApi.`when`(inductionRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveInductionResponse())
    )
  }

  protected fun singleActiveConvictionResponse(crn: String) {
    val convictionsRequest =
      request()
        .withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveConvictionResponse())
    )
  }

  protected fun convictionWithNoSentenceResponse(crn: String) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(convictionNoSentenceResponse())
    )
  }

  protected fun noConvictionsResponse(crn: String) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody("[]")
    )
  }

  protected fun singleActiveConvictionResponseForAllConvictions(crn: String) {
    val convictionsRequest =
      request()
        .withPath("/offenders/crn/$crn/convictions")

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveConvictionResponse())
    )
  }

  protected fun singleInactiveConvictionsResponse(crn: String) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions")
    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleInactiveConvictionResponse())
    )
  }

  protected fun offenderSummaryResponse(crn: String) {
    val summaryRequest =
      request().withPath("/offenders/crn/$crn")

    communityApi.`when`(summaryRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(offenderSummaryResponse())
    )
  }

  protected fun expectWorkloadAllocationCompleteMessages(crn: String, times: Int) {
    numberOfMessagesCurrentlyOnQueue(allocationCompleteClient, allocationCompleteUrl, 3 * times)
    val changeEvents = getAllAllocationMessages()
    changeEvents.forEach { changeEvent ->
      Assertions.assertEquals(crn, changeEvent.personReference.identifiers.first { it.type == "CRN" }.value)
    }
    val numberOfPersonAllocationMessages = changeEvents.count { it.eventType == "person.community.manager.allocated" }
    Assertions.assertEquals(1 * times, numberOfPersonAllocationMessages)

    val numberOfEventAllocationMessages = changeEvents.count { it.eventType == "event.manager.allocated" }
    Assertions.assertEquals(1 * times, numberOfEventAllocationMessages)

    val numberOfRequirementAllocationMessages = changeEvents.count { it.eventType == "requirement.manager.allocated" }
    Assertions.assertEquals(1 * times, numberOfRequirementAllocationMessages)
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

  protected fun staffCodeResponse(staffCode: String, teamCode: String, staffGrade: String = "PSM") {
    val request = request().withPath("/staff/staffCode/$staffCode")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(staffByCodeResponse(staffCode, teamCode, staffGrade))
    )
  }

  protected fun staffUserNameResponse(userName: String) {
    val request = request().withPath("/staff/username/$userName")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(staffByUserNameResponse(userName))
    )
  }

  protected fun offenderSearchByCrnsResponse(crns: List<String>) {
    val request = request().withPath("/crns").withBody(objectMapper.writeValueAsString(crns))
    offenderSearchApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(offenderSearchByCrnsResponse())
    )
  }

  protected fun oneOffenderReturnedWhenSearchByCrnsResponse(crns: List<String>) {
    val request = request().withPath("/crns").withBody(objectMapper.writeValueAsString(crns))
    offenderSearchApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(oneOffenderSearchByCrnsResponse())
    )
  }

  protected fun singleActiveUnpaidRequirementResponse(crn: String, convictionId: BigInteger) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveUnpaidRequirementResponse())
    )
  }

  protected fun singleActiveRequirementResponse(crn: String, convictionId: BigInteger, requirementId: BigInteger = BigInteger.valueOf(123456789L)) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveRequirementResponse(requirementId))
    )
  }

  protected fun singleActiveRequirementNoLengthResponse(crn: String, convictionId: BigInteger, requirementId: BigInteger = BigInteger.valueOf(123456789L)) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameter(Parameter("activeOnly", "true"))

    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveRequirementNoLengthResponse(requirementId))
    )
  }
}

data class SQSMessage(
  val Message: String,
  val MessageId: String
)
