package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType

@Service
class CaseTypeMapper(
  private val caseTypeRules: List<CaseTypeRule>
) {
  fun getCaseType(activeConvictions: List<Conviction>): CaseType {
    val allConvictionTypes = activeConvictions.map { conviction ->
      convictionToCaseType(conviction).let { caseType ->
        {
          conviction.convictionId to caseType
        }
      }
    }.associate { it.invoke() }

    val convictionIdFirst = activeConvictions.sortedByDescending { it.sentence?.expectedSentenceEndDate }
      .first().convictionId

    var caseType = allConvictionTypes.getValue(convictionIdFirst)
    if (allConvictionTypes.containsValue(CaseType.CUSTODY)) {
      caseType = CaseType.CUSTODY
    }
    return caseType
  }

  fun convictionToCaseType(conviction: Conviction): CaseType {
    for (caseTypeRule in caseTypeRules) {
      caseTypeRule.isCaseType(conviction.sentence?.sentenceType?.code, conviction.custody?.status?.code)?.let { return it }

      // conviction.sentence?.expectedSentenceEndDate
    }

    return CaseType.UNKNOWN
  }
}
