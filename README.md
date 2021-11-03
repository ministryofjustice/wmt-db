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

These tests are defaults from the template, and the app only serves to run the flyway scripts. This allows you to test that the scripts work locally against mssql in docker
```shell
docker compose up -d
./gradlew clean check
```

