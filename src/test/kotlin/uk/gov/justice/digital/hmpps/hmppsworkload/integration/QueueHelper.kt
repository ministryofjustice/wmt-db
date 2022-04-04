package uk.gov.justice.digital.hmpps.hmppsworkload.integration

import com.amazonaws.services.sqs.AmazonSQSAsync
import org.awaitility.kotlin.await
import org.awaitility.kotlin.matches
import org.awaitility.kotlin.untilCallTo

fun oneMessageCurrentlyOnQueue(client: AmazonSQSAsync, queueUrl: String) {
  await untilCallTo {
    getNumberOfMessagesCurrentlyOnQueue(
      client,
      queueUrl
    )
  } matches { it == 1 }
}

private fun getNumberOfMessagesCurrentlyOnQueue(client: AmazonSQSAsync, queueUrl: String): Int? {
  val queueAttributes = client.getQueueAttributes(queueUrl, listOf("ApproximateNumberOfMessages"))
  return queueAttributes.attributes["ApproximateNumberOfMessages"]?.toInt()
}
