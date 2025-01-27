package uk.gov.justice.digital.hmpps.hmppsworkload.listener

import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationEmail
import uk.gov.justice.digital.hmpps.hmppsworkload.service.NotificationService.NotificationInvalidSenderException
import uk.gov.service.notify.NotificationClientApi
import uk.gov.service.notify.NotificationClientException
import uk.gov.service.notify.SendEmailResponse

private const val MAX_RETRIES = 3

@Component
class NotificationListener(
  private val notificationClient: NotificationClientApi,
  private val objectMapper: ObjectMapper,
) {
  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }

  @SqsListener("hmppsnotificationqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun processMessage(rawMessage: String) {
    log.info("Processing message on notification queue")
    val notification = getNotification(rawMessage)
    notification.emailTo.map { email ->
      log.info("Sending email to $email")
      log.info("Email template: ${notification.emailTemplate}")
      handleError(email) {
        notificationClient.sendEmail(
          notification.emailTemplate,
          email,
          notification.emailParameters,
          notification.emailReferenceId,
        )
      }
    }
  }

  private fun handleError(emailRecipient: String, wrappedApiCall: () -> SendEmailResponse): SendEmailResponse {
    var attempt = 0
    while (true) {
      try {
        return wrappedApiCall.invoke()
      } catch (notificationException: NotificationClientException) {
        if (notificationException.httpResult == 500 && attempt < MAX_RETRIES) {
          attempt++
          log.warn("Retrying notify send for {} ", notificationException.message)
          continue
        }
        if (notificationException.httpResult == 400) {
          throw NotificationInvalidSenderException(emailRecipient, notificationException)
        }
        throw notificationException
      }
    }
  }

  private fun getNotification(rawMessage: String): NotificationEmail {
    // TODO what type is raw message ?
    // val message = objectMapper.readValue(rawMessage, SQSMessage::class.java)
    return objectMapper.readValue(rawMessage, NotificationEmail::class.java)
  }
}
