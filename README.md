# Expenses Tracker Microservice

A simple Spring Boot microservice that manages expense records and persists them to MongoDB.

This README explains how the project is organized, how to build and run it locally, the important configuration, the public API, how auditing and exception handling work, and tips for debugging.

---

## Table of Contents

- Project overview
- Prerequisites
- Build & run
- Configuration
- API endpoints
- Data model
- Auditing and created/modified fields
- Exception handling
- Development notes & troubleshooting
- Useful commands

---

## Project overview

This microservice provides a REST API to create (and later update/read) expense records. It uses Spring Boot, Spring Data MongoDB and Lombok for boilerplate reduction. The codebase uses a layered structure:

- controller/ - REST controllers (exposes endpoints)
- service/ - business logic interfaces and implementations
- repository/ - Spring Data MongoDB repositories
- model/ - domain/persistence models
- request/ - incoming request DTOs
- response/ - outgoing response DTOs
- mappers/ - conversion utilities between DTOs and models
- config/ - MongoDB configuration (auditing)
- exception/ - global exception handler

Files of interest:
- `src/main/java/com/expenses/tracker/controller/ExpensesController.java`
- `src/main/java/com/expenses/tracker/service/ExpensesService.java`
- `src/main/java/com/expenses/tracker/service/impl/ExpensesServiceImpl.java`
- `src/main/java/com/expenses/tracker/repository/ExpensesRepository.java`
- `src/main/java/com/expenses/tracker/model/ExpensesModel.java`
- `src/main/java/com/expenses/tracker/mappers/ExpensesMapper.java`
- `src/main/java/com/expenses/tracker/config/MongoConfig.java`
- `src/main/java/com/expenses/tracker/exception/GlobalExceptionHandler.java`

---

## Prerequisites

- Java 21 (project property `java.version` is set to 21)
- Maven (or use the included Maven wrapper `./mvnw`)
- MongoDB (the example `application.yaml` points to a local Mongo instance)

---

## Build & run

From the project root (this repository contains a Maven wrapper):

```bash
# Compile & package
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

Or run the generated jar:

```bash
java -jar target/expenses-tracker-0.0.1-SNAPSHOT.jar
```

By default the application reads Mongo connection settings from `src/main/resources/application.yaml`. See the Configuration section below if you need to override values.

---

## Configuration

The main configuration is in `src/main/resources/application.yaml` (Spring Boot properties):

```yaml
spring:
  application:
    name: expenses-tracker
  data:
    mongodb:
      uri: "mongodb://admin:admin123@127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&authSource=admin&appName=mongosh+2.8.2"
      database: "expenses_tracker_db"
```

You can override these properties using environment variables, for example:

```bash
export SPRING_DATA_MONGODB_URI="mongodb://user:pass@host:27017/db?authSource=admin"
export SPRING_DATA_MONGODB_DATABASE="expenses_tracker_db"
./mvnw spring-boot:run
```

Note: the project uses Spring Data MongoDB auditing. See `MongoConfig.java` where an `AuditorAware` bean is provided that sets the default auditor to `SYSTEM`. If you need different auditing behavior, update `MongoConfig`.

---

## Public API

The controller currently exposes the following endpoints under the base path `/api/v1/expenses`.

### POST /api/v1/expenses/add

Creates a new expense record.

Request body (JSON):

```json
{
  "expenseId": 1001,
  "userId": "user-123",
  "name": "Taxi to airport",
  "description": "Taxi fare",
  "amount": 25.5
}
```

Notes:
- The API accepts the fields defined in `src/main/java/com/expenses/tracker/request/ExpensesRequest.java` (it extends `CommonsRequest` which contains `expenseId` and `userId`).
- The service currently checks for an existing expense using the provided `expenseId`. To avoid duplicate errors provide a unique `expenseId` when creating a new record.

Successful response (HTTP 200) returns an `ApiResponse` wrapping an `ExpensesResponse`:

```json
{
  "message": "Expenses added successfully",
  "data": {
    "expenseId": 1001,
    "userId": "user-123",
    "name": "Taxi to airport",
    "description": "Taxi fare",
    "amount": 25.5,
    "status": "PENDING",
    "expenseDate": "2026-07-12T10:15:30+05:30",
    "modifiedDate": "2026-07-12T10:15:30+05:30",
    "createdBy": "SYSTEM",
    "updatedBy": "SYSTEM"
  }
}
```

Field mapping reference:
- Request: `ExpensesRequest` (fields: `expenseId`, `userId`, `name`, `description`, `amount`)
- Persistence model: `ExpensesModel` (fields: `id`/`expenseId`, `userId`, `name`, `description`, `amount`, `status`, `createdDate`, `updatedDate`, `createdBy`, `updatedBy`)
- Response: `ExpensesResponse` mirrors the model and formats dates to IST via `AppUtils.getISTDateFormatted(...)`.

---

## Data model

`ExpensesModel` is the persistence entity annotated with `@Document(collection = "expenses_info")`. Key fields:

- `id` (Long) - primary key
- `userId` (String)
- `name` (String)
- `description` (String)
- `amount` (double)
- `status` (enum `ExpenseStatus`)
- `createdDate`, `updatedDate` (LocalDateTime) - audited timestamps
- `createdBy`, `updatedBy` (String) - audited user identifiers

Mapping between request and model is handled by `ExpensesMapper`.

---

## Auditing and timestamps

The project enables Spring Data MongoDB auditing in `MongoConfig` with `@EnableMongoAuditing` and provides a simple `AuditorAware<String>` bean that returns the string `SYSTEM`. This ensures the `@CreatedBy` and `@LastModifiedBy` fields are populated with `SYSTEM` by default.

The `createdDate` and `updatedDate` fields are annotated with `@CreatedDate` and `@LastModifiedDate`. The mapper (`ExpensesMapper`) also initializes `createdDate` and `updatedDate` to `LocalDateTime.now()` when building the model prior to saving.

If you want to customize the auditor or supply the real authenticated user, update `MongoConfig#auditorAware()` to return the current principal (for example from Spring Security).

---

## Exception handling

A global exception handler is implemented at `src/main/java/com/expenses/tracker/exception/GlobalExceptionHandler.java`. It catches and converts common exceptions into consistent `ApiResponse` payloads with appropriate HTTP status codes, including:

- `MethodArgumentNotValidException` and `ConstraintViolationException` → 400 Bad Request
- `HttpMessageNotReadableException` (malformed JSON) → 400 Bad Request
- `IllegalArgumentException` → 400 Bad Request
- `DataAccessException` → 500 Internal Server Error (database errors)
- `RuntimeException` and generic `Exception` → 500 Internal Server Error

This ensures clients always receive a predictable JSON response structure for errors.

---

## Development notes & troubleshooting

- If you see errors related to MongoDB collation or `locale` (for example: "Field 'locale' is invalid"), check that `@Document` is used correctly. The project uses `@Document(collection = "expenses_info")` — do not pass a plain string to `collation`.

- If `createdDate` isn't being stored, check these points:
  - `MongoConfig` must have `@EnableMongoAuditing` and an `AuditorAware` bean
  - Model fields should be annotated with `@CreatedDate`/`@LastModifiedDate`
  - The mapper initializes `createdDate` and `updatedDate` before saving

- If the repository `findById(...)` is invoked with a `null` id the call may behave unexpectedly. Ensure the request contains `expenseId` when your service logic depends on it, or change service logic to generate IDs server-side.

---

## Useful commands

```bash
# Run unit tests
./mvnw test

# Build the project
./mvnw clean package

# Run the app
./mvnw spring-boot:run
```

---

## Next steps / TODOs

- Implement update, list (with pagination), and get-by-id endpoints in `ExpensesController` and corresponding service methods
- Add integration tests for repository and controller
- Replace the simple `AuditorAware` implementation with one that reads the authenticated user when security is added
- Add API documentation generation (Swagger/OpenAPI is already included via springdoc dependency)

---

## Contact / maintainers

Maintainership: Expenses Tracker Team (see repository authors)

If you need help understanding a specific class or behavior, open an issue or ask for a walkthrough of the relevant file(s).

---

Thank you for using the Expenses Tracker microservice!

