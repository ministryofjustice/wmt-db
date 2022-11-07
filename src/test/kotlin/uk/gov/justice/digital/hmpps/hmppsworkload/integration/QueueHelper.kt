package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.amazonaws.services.sqs.AmazonSQS
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo

fun numberOfMessagesCurrentlyOnQueue(client: AmazonSQS, queueUrl: String, count: Int) {
  await untilCallTo {
    getNumberOfMessagesCurrentlyOnQueue(
      client,
      queueUrl
    )
  } matches { it == count }
}

fun getNumberOfMessagesCurrentlyOnQueue(client: AmazonSQS, queueUrl: String): Int = client.getQueueAttributes(queueUrl, listOf("ApproximateNumberOfMessages", "ApproximateNumberOfMessagesNotVisible"))
  .let {
    (
      it.attributes["ApproximateNumberOfMessages"]?.toInt()
        ?: 0
      ) + (it.attributes["ApproximateNumberOfMessagesNotVisible"]?.toInt() ?: 0)
  }
