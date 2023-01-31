package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.model.PurgeQueueRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
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
import org.mockserver.model.HttpStatusCode.INTERNAL_SERVER_ERROR_500
import org.mockserver.model.MediaType.APPLICATION_JSON
import org.mockserver.model.Parameter
import org.mockserver.verify.VerificationTimes
import org.slf4j.event.Level
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsAllocationMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.event.HmppsMessage
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.WMTPointsWeightings
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.domain.WMTStaff
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.CaseCategoryEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTCaseDetailsEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WMTWorkloadEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WorkloadPointsCalculationEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.entity.WorkloadReportEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.CaseCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.PduRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.ReductionCategoryRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.ReductionReasonRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.RegionRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.TiersRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.WMTCaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.WMTWorkloadRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.WorkloadPointsCalculationRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.jpa.repository.WorkloadReportRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.choosePractitionerByTeamCodesNoCommunityPersonManagerResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.choosePractitionerByTeamResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.choosePractitionerByTeamResponseUnallocated
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.choosePractitionerStaffInMultipleTeamsResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.communityApiAssessmentResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.convictionNoSentenceResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.nomsLookupResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.notFoundTierResponse
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.responses.offenderSummaryResponse
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
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.OffenderManagerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.PduEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.RegionEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.TeamEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity.WMTWorkloadOwnerEntity
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.AdjustmentReasonRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.CaseDetailsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.EventManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.OffenderManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.PersonManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.ReductionsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.RequirementManagerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.TeamRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCMSRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTCourtReportsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WMTWorkloadOwnerRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadCalculationRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.WorkloadPointsRepository
import uk.gov.justice.digital.hmpps.hmppsworkload.listener.HmppsOffenderEvent
import uk.gov.justice.digital.hmpps.hmppsworkload.service.AuditMessage
import uk.gov.justice.hmpps.sqs.HmppsQueueService
import uk.gov.justice.hmpps.sqs.MissingQueueException
import java.math.BigDecimal
import java.math.BigInteger

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class IntegrationTestBase {

  private var oauthMock: ClientAndServer =
    startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 9090)
  var communityApi: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8092)
  var hmppsTier: ClientAndServer = startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8082)
  var assessRisksNeedsApi: ClientAndServer =
    startClientAndServer(Configuration.configuration().logLevel(Level.WARN), 8085)
  var workforceAllocationsToDeliusApi: ClientAndServer =
    startClientAndServer(Configuration.configuration().logLevel(Level.DEBUG), 8084)

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

  protected val allocationCompleteUrl by lazy {
    hmppsQueueService.findByQueueId("hmppsallocationcompletequeue")?.queueUrl
      ?: throw MissingQueueException("HmppsQueue allocationcompletequeue not found")
  }

  private val hmppsOffenderQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsoffenderqueue")
      ?: throw MissingQueueException("HmppsQueue hmppsoffenderqueue not found")
  }
  private val domainEventsTopic by lazy {
    hmppsQueueService.findByTopicId("hmppsdomaintopic")
      ?: throw MissingQueueException("HmppsTopic hmppsdomaintopic not found")
  }
  private val offenderEventTopic by lazy {
    hmppsQueueService.findByTopicId("hmppsoffendertopic")
      ?: throw MissingQueueException("HmppsTopic hmppsoffendertopic not found")
  }

  private val tierCalculationQueue by lazy {
    hmppsQueueService.findByQueueId("tiercalcqueue")
      ?: throw MissingQueueException("HmppsQueue tiercalcqueue not found")
  }
  private val hmppsAuditQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsauditqueue")
      ?: throw MissingQueueException("HmppsQueue hmppsauditqueue not found")
  }
  private val hmppsReductionsCompletedQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsreductionscompletedqueue")
      ?: throw MissingQueueException("HmppsQueue hmppsreductionsCompletedqueue not found")
  }
  private val workloadCalculationQueue by lazy {
    hmppsQueueService.findByQueueId("workloadcalculationqueue")
      ?: throw MissingQueueException("HmppsQueue workloadcalculationqueue not found")
  }
  protected val hmppsExtractPlacedQueue by lazy {
    hmppsQueueService.findByQueueId("hmppsextractplacedqueue")
      ?: throw MissingQueueException("HmppsQueue hmppsextractplacedqueue not found")
  }

  private val workloadPrisonerQueue by lazy {
    hmppsQueueService.findByQueueId("workloadprisonerqueue")
      ?: throw MissingQueueException("HmppsQueue  workloadprisonerqueue not found")
  }

  private val hmppsOffenderSqsDlqClient by lazy { hmppsOffenderQueue.sqsDlqClient as AmazonSQS }
  protected val hmppsOffenderSqsClient by lazy { hmppsOffenderQueue.sqsClient }

  protected val hmppsOffenderSnsClient by lazy { offenderEventTopic.snsClient }
  protected val hmppsOffenderTopicArn by lazy { offenderEventTopic.arn }

  protected val hmppsDomainSnsClient by lazy { domainEventsTopic.snsClient }
  protected val hmppsDomainTopicArn by lazy { domainEventsTopic.arn }

  private val tierCalculationSqsDlqClient by lazy { tierCalculationQueue.sqsDlqClient as AmazonSQS }
  protected val tierCalculationSqsClient by lazy { tierCalculationQueue.sqsClient }

  private val workloadCalculationSqsDlqClient by lazy { workloadCalculationQueue.sqsDlqClient as AmazonSQS }
  protected val workloadCalculationSqsClient by lazy { workloadCalculationQueue.sqsClient }

  protected val hmppsAuditQueueClient by lazy { hmppsAuditQueue.sqsClient }

  protected val hmppsReductionsCompletedClient by lazy { hmppsReductionsCompletedQueue.sqsClient }

  protected val hmppsExtractPlacedClient by lazy { hmppsExtractPlacedQueue.sqsClient }
  private val hmppsExtractPlacedDlqClient by lazy { hmppsExtractPlacedQueue.sqsDlqClient as AmazonSQS }

  private val workloadPrisonerSqsDlqClient by lazy { workloadPrisonerQueue.sqsDlqClient as AmazonSQS }
  protected val workloadPrisonerSqsClient by lazy { workloadPrisonerQueue.sqsClient }

  @Autowired
  protected lateinit var hmppsQueueService: HmppsQueueService

  @Autowired
  protected lateinit var tiersRepository: TiersRepository

  @Autowired
  protected lateinit var wmtCaseDetailsRepository: WMTCaseDetailsRepository

  @Autowired
  protected lateinit var workloadPointsCaseDetailsRepository: WorkloadPointsCalculationRepository

  @Autowired
  protected lateinit var wmtWorkloadRepository: WMTWorkloadRepository

  @Autowired
  protected lateinit var workloadReportRepository: WorkloadReportRepository

  @Autowired
  protected lateinit var reductionReasonRepository: ReductionReasonRepository

  @Autowired
  protected lateinit var reductionCategoryRepository: ReductionCategoryRepository

  @Autowired
  protected lateinit var regionRepository: RegionRepository

  @Autowired
  protected lateinit var pduRepository: PduRepository

  @Autowired
  protected lateinit var caseCategoryRepository: CaseCategoryRepository

  @Autowired
  protected lateinit var workloadPointsCalculationRepository: WorkloadPointsCalculationRepository

  @Autowired
  protected lateinit var workloadPointsRepository: WorkloadPointsRepository

  @BeforeEach
  fun setupDependentServices() {

    communityApi.reset()
    hmppsTier.reset()
    assessRisksNeedsApi.reset()
    workforceAllocationsToDeliusApi.reset()
    setupOauth()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
    caseDetailsRepository.deleteAll()
    wmtCourtReportsRepository.deleteAll()
    wmtcmsRepository.deleteAll()
    reductionsRepository.deleteAll()
    adjustmentReasonRepository.deleteAll()
    allocationCompleteClient.purgeQueue(PurgeQueueRequest(allocationCompleteUrl))
    hmppsOffenderSqsClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.queueUrl))
    hmppsOffenderSqsDlqClient.purgeQueue(PurgeQueueRequest(hmppsOffenderQueue.dlqUrl))
    workloadCalculationSqsClient.purgeQueue(PurgeQueueRequest(workloadCalculationQueue.queueUrl))
    workloadCalculationSqsDlqClient.purgeQueue(PurgeQueueRequest(workloadCalculationQueue.dlqUrl))
    tierCalculationSqsClient.purgeQueue(PurgeQueueRequest(tierCalculationQueue.queueUrl))
    tierCalculationSqsDlqClient.purgeQueue(PurgeQueueRequest(tierCalculationQueue.dlqUrl))
    hmppsAuditQueueClient.purgeQueue(PurgeQueueRequest(hmppsAuditQueue.queueUrl))
    hmppsReductionsCompletedClient.purgeQueue(PurgeQueueRequest(hmppsReductionsCompletedQueue.queueUrl))
    hmppsExtractPlacedClient.purgeQueue(PurgeQueueRequest(hmppsExtractPlacedQueue.queueUrl))
    hmppsExtractPlacedDlqClient.purgeQueue(PurgeQueueRequest(hmppsExtractPlacedQueue.dlqUrl))
    workloadPrisonerSqsClient.purgeQueue(PurgeQueueRequest(workloadPrisonerQueue.queueUrl))
    workloadPrisonerSqsDlqClient.purgeQueue(PurgeQueueRequest(workloadPrisonerQueue.dlqUrl))
    workloadCalculationRepository.deleteAll()
    clearWMT()
  }

  @AfterAll
  fun tearDownServer() {
    communityApi.stop()
    oauthMock.stop()
    hmppsTier.stop()
    assessRisksNeedsApi.stop()
    workforceAllocationsToDeliusApi.stop()
    personManagerRepository.deleteAll()
    eventManagerRepository.deleteAll()
    requirementManagerRepository.deleteAll()
    sentenceRepository.deleteAll()
    wmtCourtReportsRepository.deleteAll()
    wmtcmsRepository.deleteAll()
    reductionsRepository.deleteAll()
    adjustmentReasonRepository.deleteAll()
    workloadCalculationRepository.deleteAll()
  }

  fun clearWMT() {
    tiersRepository.deleteAll()
    wmtCaseDetailsRepository.deleteAll()
    caseCategoryRepository.deleteAll()
    workloadPointsCaseDetailsRepository.deleteAll()
    workloadPointsCalculationRepository.deleteAll()
    wmtWorkloadRepository.deleteAll()
    workloadReportRepository.deleteAll()
    reductionsRepository.deleteAll()
    reductionReasonRepository.deleteAll()
    reductionCategoryRepository.deleteAll()
    wmtWorkloadOwnerRepository.deleteAll()
    offenderManagerRepository.deleteAll()
    teamRepository.deleteAll()
    pduRepository.deleteAll()
    regionRepository.deleteAll()
  }

  protected fun setupCurrentWmtStaff(staffCode: String, teamCode: String, totalFilteredCustodyCases: Int = 20): WMTStaff {
    val region = regionRepository.findByCode("REGION1") ?: regionRepository.save(
      RegionEntity(
        code = "REGION1",
        description = "Region 1"
      )
    )
    val pdu = pduRepository.findByCode("PDU1") ?: pduRepository.save(
      PduEntity(
        code = "PDU1",
        description = "Local Delivery Unit (Actually a Probation Delivery Unit)",
        region = region
      )
    )
    val team = teamRepository.findByCode(teamCode) ?: teamRepository.save(
      TeamEntity(
        code = teamCode,
        description = "Team 1",
        ldu = pdu
      )
    )
    val offenderManager = offenderManagerRepository.findByCode(staffCode) ?: offenderManagerRepository.save(
      OffenderManagerEntity(
        code = staffCode,
        forename = "Jane",
        surname = "Doe",
        typeId = 1
      )
    )
    val workloadOwner = wmtWorkloadOwnerRepository.save(
      WMTWorkloadOwnerEntity(
        offenderManager = offenderManager,
        team = team,
        contractedHours = BigDecimal.valueOf(37.5)
      )
    )
    val workloadReport = workloadReportRepository.findByEffectiveFromIsNotNullAndEffectiveToIsNull()
      ?: workloadReportRepository.save((WorkloadReportEntity()))
    val wmtWorkload = wmtWorkloadRepository.save(
      WMTWorkloadEntity(
        workloadOwner = workloadOwner,
        workloadReport = workloadReport,
        totalFilteredCommunityCases = 10,
        totalFilteredCustodyCases = totalFilteredCustodyCases,
        totalFilteredLicenseCases = 5,
        institutionalReportsDueInNextThirtyDays = 5,
        totalFilteredCases = 35
      )
    )
    val currentWmtPointsWeightings = getWmtWorkloadWeightings()
    val workloadPointsCalculation = workloadPointsCalculationRepository.save(
      WorkloadPointsCalculationEntity(
        workloadReport = workloadReport,
        workloadPoints = currentWmtPointsWeightings.normalWeightings,
        t2aWorkloadPoints = currentWmtPointsWeightings.t2aWeightings,
        workload = wmtWorkload,
        totalPoints = 500,
        availablePoints = 1000
      )
    )
    return WMTStaff(offenderManager, team, workloadOwner, wmtWorkload, workloadPointsCalculation)
  }

  protected fun setupWmtManagedCase(wmtStaff: WMTStaff, tier: Tier, crn: String, caseType: CaseType) {
    val tierCategory = setupWmtCaseCategoryTier(tier)
    wmtCaseDetailsRepository.save(
      WMTCaseDetailsEntity(
        workload = wmtStaff.workload,
        crn = crn,
        tierCategory = tierCategory,
        caseType = caseType,
        teamCode = wmtStaff.team.code
      )
    )
  }

  protected fun setupWmtCaseCategoryTier(tier: Tier): CaseCategoryEntity =
    caseCategoryRepository.findByCategoryName(tier.name)
      ?: caseCategoryRepository.save(CaseCategoryEntity(categoryName = tier.name, categoryId = tier.ordinal))

  protected fun setupWmtUntiered(): CaseCategoryEntity = caseCategoryRepository.findByCategoryName("Untiered")
    ?: caseCategoryRepository.save(CaseCategoryEntity(categoryName = "Untiered", categoryId = 0))

  protected fun getWmtWorkloadWeightings(): WMTPointsWeightings = WMTPointsWeightings(
    workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(true),
    workloadPointsRepository.findFirstByIsT2AAndEffectiveToIsNullOrderByEffectiveFromDesc(false)
  )

  internal fun HttpHeaders.authToken(roles: List<String> = emptyList()) {
    this.setBearerAuth(
      jwtAuthHelper.createJwt(
        subject = "SOME_USER",
        roles = roles,
        clientId = "some-client"
      )
    )
  }

  protected fun noMessagesOnOffenderEventsQueue() {
    numberOfMessagesCurrentlyOnQueue(hmppsOffenderSqsClient, hmppsOffenderQueue.queueUrl, 0)
  }

  protected fun noMessagesOnExtractPlacedQueue() {
    numberOfMessagesCurrentlyOnQueue(hmppsExtractPlacedClient, hmppsExtractPlacedQueue.queueUrl, 0)
  }

  protected fun noMessagesOnWorkloadCalculationEventsQueue() {
    numberOfMessagesCurrentlyOnQueue(workloadCalculationSqsClient, workloadCalculationQueue.queueUrl, 0)
  }

  protected fun noMessagesOnWorkloadPrisonerQueue() {
    numberOfMessagesCurrentlyOnQueue(workloadPrisonerSqsClient, workloadPrisonerQueue.queueUrl, 0)
  }

  protected fun noMessagesOnWorkloadPrisonerDLQ() {
    numberOfMessagesCurrentlyOnQueue(workloadPrisonerSqsDlqClient, workloadPrisonerQueue.dlqUrl!!, 0)
  }

  protected fun noMessagesOnWorkloadCalculationEventsDLQ() {
    numberOfMessagesCurrentlyOnQueue(workloadCalculationSqsDlqClient, workloadCalculationQueue.dlqUrl!!, 0)
  }

  protected fun oneMessageOnWorkloadCalculationDeadLetterQueue() {
    numberOfMessagesCurrentlyOnQueue(workloadCalculationSqsDlqClient, workloadCalculationQueue.dlqUrl!!, 1)
  }

  protected fun noMessagesOnOffenderEventsDLQ() {
    numberOfMessagesCurrentlyOnQueue(hmppsOffenderSqsDlqClient, hmppsOffenderQueue.dlqUrl!!, 0)
  }

  protected fun noMessagesOnExtractPlacedDLQ() {
    numberOfMessagesCurrentlyOnQueue(hmppsExtractPlacedDlqClient, hmppsExtractPlacedQueue.dlqUrl!!, 0)
  }

  protected fun offenderEvent(crn: String, sentenceId: BigInteger? = null) = HmppsOffenderEvent(crn, sentenceId)

  protected fun jsonString(any: Any) = objectMapper.writeValueAsString(any) as String

  fun setupOauth() {
    val response = response().withContentType(APPLICATION_JSON)
      .withBody(objectMapper.writeValueAsString(mapOf("access_token" to "ABCDE", "token_type" to "bearer")))
    oauthMock.`when`(request().withPath("/auth/oauth/token")).respond(response)
  }

  protected fun choosePractitionerByTeamCodesResponse(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDeliusApi.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(choosePractitionerByTeamResponse())
    )
  }

  protected fun choosePractitionerByTeamCodesResponseUnallocated(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDeliusApi.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(choosePractitionerByTeamResponseUnallocated())
    )
  }

  protected fun choosePractitionerByTeamCodesResponseNoCommunityPersonManager(teamCodes: List<String>, crn: String) {
    val choosePractitionerRequest =
      request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDeliusApi.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(choosePractitionerByTeamCodesNoCommunityPersonManagerResponse())
    )
  }

  protected fun choosePractitionerStaffInMultipleTeamsResponse(teamCodes: List<String>, crn: String, staffCode: String) {
    val choosePractitionerRequest =
      request()
        .withPath("/allocation-demand/choose-practitioner").withQueryStringParameter("crn", crn).withQueryStringParameter("teamCode", teamCodes.joinToString(separator = ","))

    workforceAllocationsToDeliusApi.`when`(choosePractitionerRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(choosePractitionerStaffInMultipleTeamsResponse())
    )
  }

  protected fun tierCalculationResponse(crn: String, tier: String = "B3") {
    val request = request().withPath("/crn/$crn/tier")
    hmppsTier.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody("{\"tierScore\":\"${tier}\"}")
    )
  }

  protected fun assessmentCommunityApiResponse(crn: String) {
    val ogrsRequest =
      request().withPath("/offenders/crn/$crn/assessments")

    communityApi.`when`(ogrsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(communityApiAssessmentResponse())
    )
  }

  protected fun riskPredictorResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/predictors/rsr/history")
    assessRisksNeedsApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(successfulRiskPredictorResponse())
    )
  }

  protected fun riskPredictorErrorResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/predictors/rsr/history")
    assessRisksNeedsApi.`when`(request, Times.exactly(1)).respond(
      response().withStatusCode(INTERNAL_SERVER_ERROR_500.code()).withContentType(
        APPLICATION_JSON
      ).withBody("{}")
    )
  }

  protected fun verifyRiskPredictorCalled(crn: String, times: Int) {
    assessRisksNeedsApi.verify(
      request()
        .withPath("/risks/crn/$crn/predictors/rsr/history"),
      VerificationTimes.exactly(times)
    )
  }

  protected fun riskSummaryResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/summary")
    assessRisksNeedsApi.`when`(request, Times.exactly(2)).respond(
      response().withContentType(APPLICATION_JSON).withBody(successfulRiskSummaryResponse())
    )
  }

  protected fun riskSummaryErrorResponse(crn: String) {
    val request = request().withPath("/risks/crn/$crn/summary")
    assessRisksNeedsApi.`when`(request, Times.exactly(2)).respond(
      response().withStatusCode(INTERNAL_SERVER_ERROR_500.code()).withContentType(
        APPLICATION_JSON
      ).withBody("{}")
    )
  }

  protected fun verifyRiskSummaryCalled(crn: String, times: Int) {
    assessRisksNeedsApi.verify(
      request()
        .withPath("/risks/crn/$crn/summary"),
      VerificationTimes.exactly(times)
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

  protected fun forbiddenOffenderSummaryResponse(crn: String) {
    val summaryRequest =
      request().withPath("/offenders/crn/$crn")

    communityApi.`when`(summaryRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withStatusCode(FORBIDDEN.value())
    )
  }

  protected fun expectWorkloadAllocationCompleteMessages(crn: String): Map<String, HmppsMessage<HmppsAllocationMessage>> {
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

    return changeEvents.associateBy { it.eventType }
  }

  private fun getAllAllocationMessages(): List<HmppsMessage<HmppsAllocationMessage>> {
    val messages = ArrayList<HmppsMessage<HmppsAllocationMessage>>()
    while (getNumberOfMessagesCurrentlyOnQueue(allocationCompleteClient, allocationCompleteUrl) != 0) {
      val message = allocationCompleteClient.receiveMessage(allocationCompleteUrl)
      messages.addAll(
        message.messages.map {
          allocationCompleteClient.deleteMessage(allocationCompleteUrl, it.receiptHandle)
          val sqsMessage = objectMapper.readValue(it.body, SQSMessage::class.java)
          val personAllocationMessageType = object : TypeReference<HmppsMessage<HmppsAllocationMessage>>() {}
          objectMapper.readValue(sqsMessage.Message, personAllocationMessageType)
        }
      )
    }
    return messages
  }

  protected fun staffCodeResponse(staffCode: String, teamCode: String, staffGrade: String = "PSM", email: String? = "sheila.hancock@test.justice.gov.uk") {
    val request = request().withPath("/staff/staffCode/$staffCode")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(staffByCodeResponse(staffCode, teamCode, staffGrade, email))
    )
  }

  protected fun nomsLookupRespond(crn: String, nomsNumber: String) {
    val request = request().withPath("/secure/offenders/nomsNumber/$nomsNumber")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(nomsLookupResponse(crn, nomsNumber))
    )
  }

  protected fun nomsLookupNotFoundRespond(nomsNumber: String) {
    val request = request().withPath("/secure/offenders/nomsNumber/$nomsNumber")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withStatusCode(404)
    )
  }

  protected fun staffCodeErrorResponse(staffCode: String, teamCode: String) {
    val request = request().withPath("/staff/staffCode/$staffCode")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withStatusCode(503)
    )
  }

  protected fun staffUserNameResponse(userName: String) {
    val request = request().withPath("/staff/username/$userName")
    communityApi.`when`(request, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(staffByUserNameResponse(userName))
    )
  }

  protected fun singleActiveUnpaidRequirementResponse(crn: String, convictionId: BigInteger) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameters(
        Parameter("activeOnly", "true"),
        Parameter("excludeSoftDeleted", "true")
      )
    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveUnpaidRequirementResponse())
    )
  }

  protected fun singleActiveRequirementResponse(
    crn: String,
    convictionId: BigInteger,
    requirementId: BigInteger = BigInteger.valueOf(123456789L)
  ) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameters(
        Parameter("activeOnly", "true"),
        Parameter("excludeSoftDeleted", "true")
      )
    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveRequirementResponse(requirementId))
    )
  }

  protected fun singleActiveRequirementNoLengthResponse(
    crn: String,
    convictionId: BigInteger,
    requirementId: BigInteger = BigInteger.valueOf(123456789L)
  ) {
    val convictionsRequest =
      request().withPath("/offenders/crn/$crn/convictions/$convictionId/requirements").withQueryStringParameters(
        Parameter("activeOnly", "true"),
        Parameter("excludeSoftDeleted", "true")
      )
    communityApi.`when`(convictionsRequest, Times.exactly(1)).respond(
      response().withContentType(APPLICATION_JSON).withBody(singleActiveRequirementNoLengthResponse(requirementId))
    )
  }

  protected fun verifyAuditMessageOnQueue(): Boolean =
    getNumberOfMessagesCurrentlyOnQueue(hmppsAuditQueueClient, hmppsAuditQueue.queueUrl) == 1

  protected fun getAuditMessages(): AuditMessage {
    val message = hmppsAuditQueueClient.receiveMessage(hmppsAuditQueue.queueUrl)
    return message.messages.map {
      hmppsAuditQueueClient.deleteMessage(hmppsAuditQueue.queueUrl, it.receiptHandle)
      val auditDataType = object : TypeReference<AuditMessage>() {}
      objectMapper.readValue(it.body, auditDataType)
    }.first()
  }

  protected fun verifyReductionsCompletedOnQueue(): Boolean =
    getNumberOfMessagesCurrentlyOnQueue(hmppsReductionsCompletedClient, hmppsReductionsCompletedQueue.queueUrl) == 1

  protected fun getReductionsCompletedMessages(): HmppsMessage<JsonNode> {
    val message = hmppsReductionsCompletedClient.receiveMessage(hmppsReductionsCompletedQueue.queueUrl)
    return message.messages.map {
      hmppsReductionsCompletedClient.deleteMessage(hmppsReductionsCompletedQueue.queueUrl, it.receiptHandle)
      val reductionsCompletedMessageDataType = object : TypeReference<HmppsMessage<JsonNode>>() {}
      objectMapper.readValue(it.body, reductionsCompletedMessageDataType)
    }.first()
  }
}

data class SQSMessage(
  val Message: String,
  val MessageId: String
)
