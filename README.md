### About patients-service
The service is developed with Oracle OpenJdk 21, Spring Boot 3.2.4 and gradle-8.5.
For the test purposes an in-memory H2 database is used for persistence.

### Running the app
To run the spring boot application use the following command:
```
./gradlew bootRun
```

To run the application in Docker use:
```
./gradlew bootJar      
docker-compose up --build
```

To se the API docs and try the API, please navigate to Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```