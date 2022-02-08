plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "4.0.3"
  kotlin("plugin.spring") version "1.6.10"
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("uk.gov.justice.service.hmpps:hmpps-sqs-spring-boot-starter:1.0.2")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

  runtimeOnly("com.zaxxer:HikariCP")
  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.postgresql:postgresql:42.3.2")

  testImplementation("io.jsonwebtoken:jjwt:0.9.1")
}

tasks {
  compileKotlin {
    kotlinOptions {
      jvmTarget = "16"
    }
  }
}
