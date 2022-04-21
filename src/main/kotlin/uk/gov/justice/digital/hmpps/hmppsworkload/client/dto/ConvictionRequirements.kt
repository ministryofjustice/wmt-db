package uk.gov.justice.digital.hmpps.hmppsworkload.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigInteger

data class ConvictionRequirements @JsonCreator constructor(
  val requirements: List<ConvictionRequirement>
)

data class ConvictionRequirement @JsonCreator constructor(
  val requirementTypeMainCategory: RequirementCategory,
  val requirementTypeSubCategory: RequirementCategory,
  val requirementId: BigInteger,
  val length: BigInteger,
  val lengthUnit: String
)

data class RequirementCategory @JsonCreator constructor(
  val code: String,
  val description: String
)
