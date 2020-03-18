# eclass-service
Spring-Boot Micro-Service providing a IEC 61360 compatible taxonomy

## requirements

A running PostgreSQL Database is required. The Database Schema can be found in 

```
./src/main/resources/eClassPostgres.sql
```

## data 
The eClass CSV (Downloads from eClass Sites) files can be loaded into the database. See [instructions](http://www.postgresqltutorial.com/import-csv-file-into-posgresql-table/) on how to import CSV files.


### Service build and startup

 ```
 mvn clean spring-boot:run
 ```

  
 @TODO: point to the proper service
 The Service may be verified online [Nimble-Staging](http://nimble-staging.salzburgresearch.at/index/actuator/info)
 
