package uk.gov.justice.digital.hmpps.hmppsworkload.mapper

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.client.dto.Conviction
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.CaseType
import java.math.BigInteger

@Service
class CaseTypeMapper(
  private val caseTypeRules: List<CaseTypeRule>
) {
  fun getCaseType(activeConvictions: List<Conviction>, convictionId: BigInteger): CaseType {
    val allConvictionTypes = activeConvictions.map { conviction ->
      convictionToCaseType(conviction).let { caseType ->
        {
          conviction.convictionId to caseType
        }
      }
    }.associate { it.invoke() }
    var caseType = allConvictionTypes.getValue(convictionId)
    if (allConvictionTypes.containsValue(CaseType.CUSTODY)) {
      caseType = CaseType.CUSTODY
    }
    return caseType
  }

  fun convictionToCaseType(conviction: Conviction): CaseType {
    var caseType: CaseType = CaseType.UNKNOWN
    for (caseTypeRule in caseTypeRules) {
      if (caseTypeRule.isCaseType(conviction.sentence!!.sentenceType.code, conviction.custody?.status?.code)) {
        caseType = caseTypeRule.getCaseType()
        break
      }
    }
    return caseType
  }
}
