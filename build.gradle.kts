plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "3.3.0"
  kotlin("plugin.spring") version "1.5.10"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.microsoft.sqlserver:mssql-jdbc:9.2.1.jre15")
  implementation("org.springframework.boot:spring-boot-starter-jdbc")

  runtimeOnly("com.zaxxer:HikariCP:3.4.5")
  runtimeOnly("org.flywaydb:flyway-core:6.5.6")


}

tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = "16"
    }
  }
}
