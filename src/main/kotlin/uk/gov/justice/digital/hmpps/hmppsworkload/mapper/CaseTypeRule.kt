package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

interface CaseTypeRule {
  fun isCaseType(sentenceTypeCode: String?, custodialStatusCode: String?): Boolean
  fun getCaseType(): CaseType
}
