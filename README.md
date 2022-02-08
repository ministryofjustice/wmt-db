# hmpps-workload

Currently exists to deploy database changes to the hmpps-workload Postgres DB

## Instructions

To run locally use docker to create a Mssql instance with the following command:

```shell
docker compose up -d
```

Then execute the command 
```shell
./gradlew bootRun
```

## testing

These tests are defaults from the template, and the app only serves to run the flyway scripts. This allows you to test that the scripts work locally against postgresql in docker
```shell
docker compose up -d
./gradlew clean check
```

## Code style & formatting
```shell
./gradlew ktlintApplyToIdea addKtlintFormatGitPreCommitHook
```
will apply ktlint styles to intellij and also add a pre-commit hook to format all changed kotlin files.