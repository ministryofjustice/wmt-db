package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID

@Service
@ConditionalOnProperty(prefix = "hmpps.sqs.topics.hmppsallocationcompletetopic", name = ["arn"], matchIfMissing = true)
class LogSuccessUpdater : SuccessUpdater {

  override fun updatePerson(crn: String, allocationId: UUID, timeUpdated: ZonedDateTime) {
    log.info("crn {} with allocationId {} updated at {}", crn, allocationId, timeUpdated)
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}
