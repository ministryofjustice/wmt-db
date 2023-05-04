package uk.gov.justice.digital.hmpps.hmppsworkload.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import uk.gov.justice.digital.hmpps.hmppsworkload.domain.Tier
import java.math.BigInteger

@Embeddable
data class CommunityTierPoints(
  @Column(name = "comm_tier_1")
  val A3Points: BigInteger,
  @Column(name = "comm_tier_2")
  val A2Points: BigInteger,
  @Column(name = "comm_tier_3")
  val A1Points: BigInteger,
  @Column(name = "comm_tier_4")
  val A0Points: BigInteger,
  @Column(name = "comm_tier_5")
  val B3Points: BigInteger,
  @Column(name = "comm_tier_6")
  val B2Points: BigInteger,
  @Column(name = "comm_tier_7")
  val B1Points: BigInteger,
  @Column(name = "comm_tier_8")
  val B0Points: BigInteger,
  @Column(name = "comm_tier_9")
  val C3Points: BigInteger,
  @Column(name = "comm_tier_10")
  val C2Points: BigInteger,
  @Column(name = "comm_tier_11")
  val C1Points: BigInteger,
  @Column(name = "comm_tier_12")
  val C0Points: BigInteger,
  @Column(name = "comm_tier_13")
  val D3Points: BigInteger,
  @Column(name = "comm_tier_14")
  val D2Points: BigInteger,
  @Column(name = "comm_tier_15")
  val D1Points: BigInteger,
  @Column(name = "comm_tier_16")
  val D0Points: BigInteger,
) {
  fun asMap(): Map<Tier, BigInteger> {
    return mapOf(
      Tier.A3 to A3Points,
      Tier.A2 to A2Points,
      Tier.A1 to A1Points,
      Tier.A0 to A0Points,
      Tier.B3 to B3Points,
      Tier.B2 to B2Points,
      Tier.B1 to B1Points,
      Tier.B0 to B0Points,
      Tier.C3 to C3Points,
      Tier.C2 to C2Points,
      Tier.C1 to C1Points,
      Tier.C0 to C0Points,
      Tier.D3 to D3Points,
      Tier.D2 to D2Points,
      Tier.D1 to D1Points,
      Tier.D0 to D0Points,
    )
  }
}
