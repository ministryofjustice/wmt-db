#!/bin/bash

sleep 10s

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'yourStrong(!)Password' -d master -i /dbdata/create-database.sql
