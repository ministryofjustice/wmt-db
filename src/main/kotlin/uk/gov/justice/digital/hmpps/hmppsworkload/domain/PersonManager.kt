package uk.gov.justice.digital.hmpps.hmppsworkload.domain

data class PersonManager(val staffCode: String, val teamCode: String, val providerCode: String, val staffGrade: String) {
  constructor(staffCode: String, teamCode: String) : this(staffCode, teamCode, "", "")
  constructor(staffCode: String, teamCode: String, staffGrade: String) : this(staffCode, teamCode, "", staffGrade)
}
