package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CaseTypeMapper(
  private val caseTypeRules: List<CaseTypeRule>
) {
  fun getCaseType(activeConvictions: List<Conviction>): CaseType {
    return when {
      activeConvictions.any { convictionToCaseType(it) == CaseType.CUSTODY } ->
        CaseType.CUSTODY
      activeConvictions.isEmpty() ->
        CaseType.UNKNOWN
      else ->
        convictionToCaseType(
          activeConvictions.sortedByDescending { it.sentence?.expectedSentenceEndDate }
            .first()
        )
    }
  }

  fun convictionToCaseType(conviction: Conviction): CaseType {
    for (caseTypeRule in caseTypeRules) {
      caseTypeRule.isCaseType(conviction.sentence?.sentenceType?.code, conviction.custody?.status?.code)?.let { return it }
    }

    return CaseType.UNKNOWN
  }
}
