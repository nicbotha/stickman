# stickman
This project contains the minimum code to run a Java application with a few basic features. 

Clone and run
`mvn clean install -Dspring.profiles.active="test"`

## Model
Contains all persistence related code and configuration. 

### Features 
- Single-Table multi-tenancy
- Entity field validation with I18N
- DBUnit tests running with Derby
- Spring profiles to switch between test/dev/prod datasources

### Technologies & Libraries
- JPA EclipseLink 
- Spring Data
- Hibernate Validator
- DBUnit
