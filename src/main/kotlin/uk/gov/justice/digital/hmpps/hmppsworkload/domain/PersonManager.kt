package uk.gov.justice.digital.hmpps.hmppsworkload.domain

data class PersonManager(val staffCode: String, val teamCode: String, val providerCode: String, val staffGrade: String) {
  constructor(personManagerIdentifier: PersonManagerIdentifier, providerCode: String, staffGrade: String) :
    this(personManagerIdentifier.staffCode, personManagerIdentifier.teamCode, providerCode, staffGrade)
}
