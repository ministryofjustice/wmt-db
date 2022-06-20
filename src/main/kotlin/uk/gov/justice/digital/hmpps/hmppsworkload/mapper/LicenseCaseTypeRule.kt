package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class LicenseCaseTypeRule : CaseTypeRule {

  private val custodialStatusCodes = setOf("A", "C", "D", "R", "I", "AT")

  override fun isCaseType(sentenceTypeCode: String?, custodialStatusCode: String?): CaseType? =
    if ("SC" == sentenceTypeCode && !custodialStatusCodes.contains(custodialStatusCode)) CaseType.LICENSE else null
}
