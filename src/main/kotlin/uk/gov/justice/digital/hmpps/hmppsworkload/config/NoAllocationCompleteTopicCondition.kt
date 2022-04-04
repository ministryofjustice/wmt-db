package uk.gov.justice.digital.hmpps.hmppsworkload.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions
import org.springframework.context.annotation.ConfigurationCondition

class NoAllocationCompleteTopicCondition : NoneNestedConditions(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION) {

  @ConditionalOnProperty("hmpps.sqs.topics.hmppsallocationcompletetopic.arn")
  class AllocationCompleteCondition
}
