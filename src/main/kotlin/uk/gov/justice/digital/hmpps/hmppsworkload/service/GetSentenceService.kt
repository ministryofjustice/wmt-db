package uk.gov.justice.digital.hmpps.hmppsworkload.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppsworkload.jpa.repository.SentenceRepository
import java.math.BigInteger
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@Service
class GetSentenceService(private val sentenceRepository: SentenceRepository) {

  fun getCasesDueToEndCount(crns: List<String>): BigInteger = sentenceRepository.findByCrnInAndExpectedEndDateGreaterThanEqual(
    crns,
    ZonedDateTime.now().truncatedTo(
      ChronoUnit.DAYS
    )
  ).let { sentences ->
    val beforeEndDate = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(28L)
    val casesDueToEnd = sentences
      .groupBy { sentence -> sentence.crn }
      .mapValues { sentence -> sentence.value.maxOf { it.expectedEndDate } }
      .filter { sentence -> sentence.value.isEqual(beforeEndDate) || !sentence.value.isAfter(beforeEndDate) }
      .count()
    casesDueToEnd.toBigInteger()
  }

  fun getCasesDueToBeReleases(crns: List<String>): BigInteger = sentenceRepository.findByCrnInAndExpectedReleaseDateGreaterThanEqual(crns, ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)).let { sentences ->
    val beforeEndDate = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(28L)
    val releasesDueToEnd = sentences
      .groupBy { sentence -> sentence.crn }
      .mapValues { sentence -> sentence.value.maxOf { it.expectedReleaseDate ?: ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()) } }
      .filter { sentence -> sentence.value.isEqual(beforeEndDate) || !sentence.value.isAfter(beforeEndDate) }
      .count()
    releasesDueToEnd.toBigInteger()
  }
}
