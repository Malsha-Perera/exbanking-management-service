
# exbanking management service

This project is a gRPC protobuf-based application developed using Java 11 and Spring Boot. It utilizes Maven for its build process.

## Prerequisites

- Java 11
- Maven 3.8.1

## Building the Project

To build the project, use the following Maven command:

```sh
mvn clean install
```

This command will compile the project and create an executable JAR file.


## Running the Application

After building the project, you can start the application using the following command:


```sh
cd target
java -jar exbanking-management-service-0.0.1-SNAPSHOT
```

Application RPC server starts and listens on port `9090`, ready to accept incoming gRPC requests.


## Application Features

- At startup, the service automatically creates two users with two active bank accounts as well an inactive bank account
- Every flow request is validated before processing to ensure data integrity and adherence to business rules.
- Proper exceptions with descriptive validation messages are thrown to the user for negative scenarios, enhancing the user experience and ease of debugging.
- Instead of an actual database, the `DatabaseRepository` class maintains several in-memory maps that mock the database collections, providing a simulated database environment.


