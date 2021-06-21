# hmpps-workload

Currently exists to deploy database changes to the hmpps-workload SQL Server

# Instructions

To run locally use docker to create a Mssql instance with the following command:

```
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=<A Strong Password>' -p 1433:1433 -d --name hmpps-workload  mcr.microsoft.com/mssql/server:2019-CU11-ubuntu-20.04
```

Then execute the command `./gradlew bootRun`

