package uk.gov.justice.digital.hmpps.hmppsworkload.integration.listener

import org.junit.jupiter.api.Test
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import uk.gov.justice.digital.hmpps.hmppsworkload.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationEmail
import java.util.*

class NotificationEventListenerTest : IntegrationTestBase() {

  @Test
  fun `must listen to notification event`() {
    placeMessageOnNotificationQueue()

    noMessagesOnNotificationQueue()
    noMessagesOnNotificationQueueDLQ()
  }

  private fun placeMessageOnNotificationQueue() {
    var notificationEmail = NotificationEmail(setOf("gary@fred.com", "harry.potter@warner.com"), UUID.randomUUID().toString(), "test", mapOf("george" to "mildred"))
    var messageBody = objectMapper.writeValueAsString(notificationEmail)
    notificationSqsClient.sendMessage(
      SendMessageRequest.builder().queueUrl(notificationQueue.queueUrl).messageBody(messageBody).build(),
    ).get()
  }
}
