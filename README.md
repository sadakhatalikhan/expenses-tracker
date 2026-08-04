# Expenses Tracker Microservice

A comprehensive Spring Boot microservice that manages user authentication and expense records with secure JWT-based authentication, persisting all data to MongoDB.

This README explains how the project is organized, how to build and run it locally, the important configuration, the public API, security features, how auditing and exception handling work, and tips for debugging.

---

## Table of Contents

- Project overview
- Prerequisites
- Build & run
- Configuration
- Security
- API endpoints
- Data model
- Auditing and created/modified fields
- Exception handling
- Development notes & troubleshooting
- Useful commands

---

## Project overview

This microservice provides a secure REST API to manage user authentication and expense records. It uses Spring Boot, Spring Security (JWT-based), Spring Data MongoDB, and Lombok for boilerplate reduction. The codebase uses a layered architecture:

- controller/ - REST controllers (exposes endpoints)
- service/ - business logic interfaces and implementations
- repository/ - Spring Data MongoDB repositories
- model/ - domain/persistence models
- request/ - incoming request DTOs
- response/ - outgoing response DTOs
- mappers/ - conversion utilities between DTOs and models
- config/ - MongoDB and Spring Security configuration
- security/ - JWT authentication and authorization components
- exception/ - global exception handler
- enums/ - status enumerations

Files of interest:
- `src/main/java/com/expenses/tracker/controller/AuthenticationController.java` - User login endpoint
- `src/main/java/com/expenses/tracker/controller/UserController.java` - User registration endpoint
- `src/main/java/com/expenses/tracker/controller/ExpensesController.java` - Expense management endpoints
- `src/main/java/com/expenses/tracker/security/JwtService.java` - JWT token generation and validation
- `src/main/java/com/expenses/tracker/security/JwtAuthFilter.java` - JWT request filter
- `src/main/java/com/expenses/tracker/config/SecurityConfig.java` - Spring Security configuration
- `src/main/java/com/expenses/tracker/service/UserService.java` - User authentication and registration
- `src/main/java/com/expenses/tracker/service/ExpensesService.java` - Expense management business logic
- `src/main/java/com/expenses/tracker/repository/ExpensesRepository.java` - Expense data access layer
- `src/main/java/com/expenses/tracker/repository/UserRepository.java` - User data access layer
- `src/main/java/com/expenses/tracker/model/ExpensesModel.java` - Expense persistence model
- `src/main/java/com/expenses/tracker/model/UserModel.java` - User persistence model
- `src/main/java/com/expenses/tracker/config/MongoConfig.java` - MongoDB auditing configuration
- `src/main/java/com/expenses/tracker/exception/GlobalExceptionHandler.java` - Centralized exception handling

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

## Security

The application implements **JWT-based (JSON Web Token) authentication** for secure access to protected resources. All API requests (except registration and login) require a valid JWT token in the `Authorization` header.

### Security Architecture

**Components:**

1. **JwtService** (`src/main/java/com/expenses/tracker/security/JwtService.java`)
   - Generates JWT tokens on successful authentication
   - Validates JWT tokens on each request
   - Extracts username and expiration details from tokens
   - Uses HS256 (HMAC SHA-256) algorithm for signing
   - Token expiration: 30 minutes

2. **JwtAuthFilter** (`src/main/java/com/expenses/tracker/security/JwtAuthFilter.java`)
   - Intercepts all incoming HTTP requests
   - Extracts JWT token from `Authorization: Bearer <token>` header
   - Validates token and sets authentication in Spring Security context
   - Allows stateless session management (no server-side session storage)

3. **SecurityConfig** (`src/main/java/com/expenses/tracker/config/SecurityConfig.java`)
   - Enables Spring Security with method-level authorization
   - Disables CSRF protection (safe for stateless JWT APIs)
   - Configures stateless session management (SessionCreationPolicy.STATELESS)
   - Defines public and protected endpoints:
     - **Public (no authentication required):**
       - `POST /api/v1/auth/user/login` - User login
       - `POST /api/v1/users/add` - User registration
     - **Protected (authentication required):**
       - `GET/POST/PUT /api/v1/expenses/**` - All expense endpoints

4. **Password Encoding**
   - Uses BCrypt password hashing algorithm
   - Passwords are never stored in plain text
   - Verified during authentication using BCrypt comparison

### Authentication Flow

```
1. Client registers: POST /api/v1/users/add (UserRequest: username, phoneNumber, password)
   → Password is hashed with BCrypt
   → User stored in MongoDB
   → Returns UserResponse

2. Client logs in: POST /api/v1/auth/user/login (AuthRequest: phoneNumber, password)
   → Credentials validated against stored user
   → JWT token generated with 30-minute expiration
   → Returns JwtResponse containing token

3. Client makes request: GET /api/v1/expenses/all
   → Includes header: Authorization: Bearer <jwt_token>
   → JwtAuthFilter validates token
   → Request proceeds if token is valid and not expired
   → Returns response wrapped in ApiResponse

4. Token expiration: After 30 minutes, client must login again
   → Login returns new JWT token
```

### Endpoint Security Summary

| Endpoint | Method | Auth Required | Purpose |
|----------|--------|---------------|---------|
| `/api/v1/users/add` | POST | ❌ No | Register new user |
| `/api/v1/auth/user/login` | POST | ❌ No | Authenticate and get JWT |
| `/api/v1/expenses/add` | POST | ✅ Yes | Create new expense |
| `/api/v1/expenses/all` | GET | ✅ Yes | Retrieve all expenses |
| `/api/v1/expenses/user/{userId}` | GET | ✅ Yes | Get expenses by user |
| `/api/v1/expenses/{expenseId}` | GET | ✅ Yes | Get specific expense |
| `/api/v1/expenses/status/{status}` | GET | ✅ Yes | Get expenses by status |
| `/api/v1/expenses/user/status/{userId}/{status}` | GET | ✅ Yes | Get user expenses by status |
| `/api/v1/expenses/update-status` | PUT | ✅ Yes | Update expense status |

### Example Requests

**1. User Registration:**

```bash
curl -X POST http://localhost:8080/api/v1/users/add \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "phoneNumber": "9876543210",
    "password": "SecurePassword123!"
  }'
```

Response:
```json
{
  "message": "User added successfully",
  "data": {
    "id": "ObjectId",
    "userId": 1001,
    "username": "john_doe",
    "phoneNumber": "9876543210",
    "userStatus": "ACTIVE",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30",
    "createdBy": "SYSTEM",
    "updatedBy": "SYSTEM"
  }
}
```

**2. User Login:**

```bash
curl -X POST http://localhost:8080/api/v1/auth/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "9876543210",
    "password": "SecurePassword123!"
  }'
```

Response:
```json
{
  "message": "success",
  "data": {
    "type": "Bearer",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "id": "ObjectId",
    "name": "john_doe",
    "phoneNumber": "9876543210",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30"
  }
}
```

**3. Protected Endpoint (with JWT):**

```bash
curl -X GET http://localhost:8080/api/v1/expenses/all \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Response:
```json
{
  "message": "Expenses loaded successfully",
  "data": [
    {
      "id": 1,
      "userId": 1001,
      "name": "Taxi to airport",
      "description": "Taxi fare",
      "amount": 25.5,
      "status": "PENDING",
      "createdDate": "2026-08-04T07:16:42+05:30",
      "updatedDate": "2026-08-04T07:16:42+05:30",
      "createdBy": "john_doe",
      "updatedBy": "john_doe"
    }
  ]
}
```

### Security Best Practices Implemented

✅ **Password Security**: BCrypt hashing with automatic salting
✅ **Token Security**: JWT signed with HS256 algorithm
✅ **Token Expiration**: 30-minute token lifetime
✅ **Stateless Architecture**: No server-side session storage (scalable)
✅ **CSRF Protection**: Disabled for stateless API (safe with JWT)
✅ **Unique Constraints**: Prevents duplicate users (username & phone number)
✅ **Exception Handling**: Consistent error responses with HTTP status codes
✅ **Request Validation**: Input validation on all endpoints

### Security Configuration Details

The security configuration is defined in `SecurityConfig.java`:

- **Session Management**: Stateless (no cookies, no session tracking)
- **Authentication Provider**: DaoAuthenticationProvider with BCrypt
- **Filter Chain**: JwtAuthFilter runs before UsernamePasswordAuthenticationFilter
- **CSRF**: Disabled (safe for REST APIs with token-based auth)
- **Method Security**: Enabled for role-based access control (@PreAuthorize, @PostAuthorize)

---

## User Management

The application supports complete user lifecycle management:

### User Registration (POST /api/v1/users/add)

Creates a new user in the system with validation for duplicate users.

**Request:**
```json
{
  "username": "john_doe",
  "phoneNumber": "9876543210",
  "password": "SecurePassword123!"
}
```

**Validation:**
- Phone number must be unique (throws: "User with phone number X already exists")
- Username must be unique (throws: "User with username X already exists")
- Password is automatically hashed using BCrypt

**Response:**
```json
{
  "message": "User added successfully",
  "data": {
    "id": "MongoDB_ObjectId",
    "userId": 1001,
    "username": "john_doe",
    "phoneNumber": "9876543210",
    "userStatus": "ACTIVE",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30",
    "createdBy": "SYSTEM",
    "updatedBy": "SYSTEM"
  }
}
```

### User Authentication (POST /api/v1/auth/user/login)

Authenticates user with phone number and password, returns JWT token.

**Request:**
```json
{
  "phoneNumber": "9876543210",
  "password": "SecurePassword123!"
}
```

**Process:**
1. AuthenticationManager validates credentials against stored user
2. Compares provided password with stored BCrypt hash
3. Generates JWT token (valid for 30 minutes)
4. Returns token in JwtResponse

**Response (Success):**
```json
{
  "message": "success",
  "data": {
    "type": "Bearer",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "id": "MongoDB_ObjectId",
    "name": "john_doe",
    "phoneNumber": "9876543210",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30"
  }
}
```

**Response (Failure - 401 Unauthorized):**
```json
{
  "message": "invalid user request !",
  "data": null
}
```

### User Data Model

Users are stored in MongoDB collection `user_info` with the following structure:

```java
@Document(collection = "user_info")
public class UserModel {
    @Id
    private String id;                      // MongoDB ObjectId (generated)
    private Long userId;                    // Sequential numeric ID (generated)
    private String username;                // Unique username
    private String password;                // BCrypt hashed password
    private String phoneNumber;             // Unique phone number
    private UserStatus userStatus;          // ACTIVE, INACTIVE, SUSPENDED
    @CreatedDate
    private LocalDateTime createdDate;      // Auto-set on creation
    @LastModifiedDate
    private LocalDateTime updatedDate;      // Auto-updated on modification
    @CreatedBy
    private String createdBy;               // Auditor (SYSTEM or username)
    @LastModifiedBy
    private String updatedBy;               // Auditor (SYSTEM or username)
}
```

---

## Expense API

The controller exposes the following endpoints under the base path `/api/v1/expenses` for managing expense records. All expense endpoints require JWT authentication.

### POST /api/v1/expenses/add

Creates a new expense record.

Request body (JSON):

```json
{
  "userId": 1001,
  "name": "Taxi to airport",
  "description": "Taxi fare",
  "amount": 25.5
}
```

Notes:
- The API accepts the fields defined in `src/main/java/com/expenses/tracker/request/ExpensesRequest.java`.
- Expense ID is generated automatically by SequenceGeneratorService (not provided by client).
- Status defaults to `PENDING` when created.
- Requires JWT authentication in Authorization header.

Successful response (HTTP 200) returns an `ApiResponse` wrapping an `ExpensesResponse`:

```json
{
  "message": "Expenses added successfully",
  "data": {
    "id": 1001,
    "userId": 1001,
    "name": "Taxi to airport",
    "description": "Taxi fare",
    "amount": 25.5,
    "status": "PENDING",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30",
    "createdBy": "john_doe",
    "updatedBy": "john_doe"
  }
}
```

### GET /api/v1/expenses/all

Retrieves all expenses from the system.

Request:
- Requires JWT authentication
- No request body

Response (HTTP 200):

```json
{
  "message": "Expenses loaded successfully",
  "data": [
    {
      "id": 1001,
      "userId": 1001,
      "name": "Taxi to airport",
      "description": "Taxi fare",
      "amount": 25.5,
      "status": "PENDING",
      "createdDate": "2026-08-04T07:16:42+05:30",
      "updatedDate": "2026-08-04T07:16:42+05:30",
      "createdBy": "john_doe",
      "updatedBy": "john_doe"
    },
    {
      "id": 1002,
      "userId": 1002,
      "name": "Lunch",
      "description": "Team lunch",
      "amount": 45.0,
      "status": "APPROVED",
      "createdDate": "2026-08-03T12:00:00+05:30",
      "updatedDate": "2026-08-03T13:00:00+05:30",
      "createdBy": "jane_smith",
      "updatedBy": "admin"
    }
  ]
}
```

### GET /api/v1/expenses/user/{userId}

Retrieves all expenses for a specific user based on userId.

Request:
- Path parameter: `userId` (numeric)
- Requires JWT authentication

Response (HTTP 200):

```json
{
  "message": "Expenses loaded successfully",
  "data": [
    {
      "id": 1001,
      "userId": 1001,
      "name": "Taxi to airport",
      "description": "Taxi fare",
      "amount": 25.5,
      "status": "PENDING",
      "createdDate": "2026-08-04T07:16:42+05:30",
      "updatedDate": "2026-08-04T07:16:42+05:30",
      "createdBy": "john_doe",
      "updatedBy": "john_doe"
    }
  ]
}
```

### GET /api/v1/expenses/{expenseId}

Retrieves a specific expense by expense ID.

Request:
- Path parameter: `expenseId` (numeric)
- Requires JWT authentication

Response (HTTP 200):

```json
{
  "message": "Expenses loaded successfully",
  "data": {
    "id": 1001,
    "userId": 1001,
    "name": "Taxi to airport",
    "description": "Taxi fare",
    "amount": 25.5,
    "status": "PENDING",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30",
    "createdBy": "john_doe",
    "updatedBy": "john_doe"
  }
}
```

Response (HTTP 404 - Not Found):

```json
{
  "message": "Expense not found",
  "data": null
}
```

### GET /api/v1/expenses/status/{status}

Retrieves all expenses with a specific status.

Request:
- Path parameter: `status` (enum: PENDING, APPROVED, REJECTED, CANCELLED)
- Requires JWT authentication

Response (HTTP 200):

```json
{
  "message": "Expenses loaded successfully",
  "data": [
    {
      "id": 1001,
      "userId": 1001,
      "name": "Taxi to airport",
      "description": "Taxi fare",
      "amount": 25.5,
      "status": "PENDING",
      "createdDate": "2026-08-04T07:16:42+05:30",
      "updatedDate": "2026-08-04T07:16:42+05:30",
      "createdBy": "john_doe",
      "updatedBy": "john_doe"
    }
  ]
}
```

### GET /api/v1/expenses/user/status/{userId}/{status}

Retrieves expenses for a specific user filtered by status.

Request:
- Path parameters: `userId` (numeric), `status` (enum: PENDING, APPROVED, REJECTED, CANCELLED)
- Requires JWT authentication

Response (HTTP 200):

```json
{
  "message": "Expenses loaded successfully",
  "data": [
    {
      "id": 1001,
      "userId": 1001,
      "name": "Taxi to airport",
      "description": "Taxi fare",
      "amount": 25.5,
      "status": "PENDING",
      "createdDate": "2026-08-04T07:16:42+05:30",
      "updatedDate": "2026-08-04T07:16:42+05:30",
      "createdBy": "john_doe",
      "updatedBy": "john_doe"
    }
  ]
}
```

### PUT /api/v1/expenses/update-status

Updates the status of an existing expense.

Request body (JSON):

```json
{
  "expenseId": 1001,
  "status": "APPROVED"
}
```

Notes:
- Valid status values: PENDING, APPROVED, REJECTED, CANCELLED
- Requires JWT authentication
- Returns 404 if expense not found

Successful response (HTTP 200):

```json
{
  "message": "Expenses status updated successfully",
  "data": {
    "id": 1001,
    "userId": 1001,
    "name": "Taxi to airport",
    "description": "Taxi fare",
    "amount": 25.5,
    "status": "APPROVED",
    "createdDate": "2026-08-04T07:16:42+05:30",
    "updatedDate": "2026-08-04T07:16:42+05:30",
    "createdBy": "john_doe",
    "updatedBy": "admin"
  }
}
```

---

## Data model

### Expense Model

`ExpensesModel` is the persistence entity annotated with `@Document(collection = "expenses_info")`:

```java
@Document(collection = "expenses_info")
public class ExpensesModel {
    @Id
    private Long id;                        // Primary key (auto-generated)
    private Long userId;                    // User ID (foreign key reference)
    private String name;                    // Expense name
    private String description;             // Expense description
    private double amount;                  // Expense amount
    private ExpenseStatus status;           // Status: PENDING, APPROVED, REJECTED, CANCELLED
    @CreatedDate
    private LocalDateTime createdDate;      // Creation timestamp (auto-set)
    @LastModifiedDate
    private LocalDateTime updatedDate;      // Last modification timestamp (auto-updated)
    @CreatedBy
    private String createdBy;               // Auditor on creation (e.g., username or SYSTEM)
    @LastModifiedBy
    private String updatedBy;               // Auditor on modification (e.g., username or SYSTEM)
}
```

Key relationships:
- `userId` references the `UserModel.userId` field
- `status` enum has values: PENDING, APPROVED, REJECTED, CANCELLED
- Auditing fields (`createdBy`, `updatedBy`) are automatically populated based on authenticated user

### User Model

`UserModel` is the persistence entity annotated with `@Document(collection = "user_info")`:

```java
@Document(collection = "user_info")
public class UserModel {
    @Id
    private String id;                      // MongoDB ObjectId (generated by MongoDB)
    private Long userId;                    // Sequential numeric ID (auto-generated by SequenceGeneratorService)
    private String username;                // Unique username (constraint enforced in service)
    private String password;                // BCrypt hashed password
    private String phoneNumber;             // Unique phone number (constraint enforced in service)
    private UserStatus userStatus;          // Status: ACTIVE, INACTIVE, SUSPENDED
    @CreatedDate
    private LocalDateTime createdDate;      // Creation timestamp (auto-set)
    @LastModifiedDate
    private LocalDateTime updatedDate;      // Last modification timestamp (auto-updated)
    @CreatedBy
    private String createdBy;               // Auditor on creation (SYSTEM or username)
    @LastModifiedBy
    private String updatedBy;               // Auditor on modification (SYSTEM or username)
}
```

Key constraints:
- `username` is unique (RuntimeException thrown on duplicate)
- `phoneNumber` is unique (RuntimeException thrown on duplicate)
- `password` is stored as BCrypt hash (never in plain text)
- `userId` is auto-incremented using SequenceGeneratorService

Mapping between DTOs and models is handled by `ExpensesMapper` and `UserDetailsMapper`.

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

### MongoDB Issues

- If you see errors related to MongoDB collation or `locale` (for example: "Field 'locale' is invalid"), check that `@Document` is used correctly. The project uses `@Document(collection = "expenses_info")` — do not pass a plain string to `collation`.

- If `createdDate` isn't being stored, check these points:
  - `MongoConfig` must have `@EnableMongoAuditing` and an `AuditorAware` bean
  - Model fields should be annotated with `@CreatedDate`/`@LastModifiedDate`
  - The mapper initializes `createdDate` and `updatedDate` before saving

- If the repository `findById(...)` is invoked with a `null` id the call may behave unexpectedly. Ensure the request contains valid ID values, or change service logic to generate IDs server-side.

### Security & Authentication Issues

- **"401 Unauthorized" responses:**
  - Verify JWT token is included in Authorization header: `Authorization: Bearer <token>`
  - Check token hasn't expired (default expiration: 30 minutes)
  - Try logging in again to get a fresh token: `POST /api/v1/auth/user/login`

- **"Invalid user request!" error on login:**
  - Verify phone number and password are correct
  - Phone number must match exactly (check for leading/trailing spaces)
  - User must have been created first via registration endpoint

- **"User with phone number X already exists" on registration:**
  - Phone number must be unique across all users
  - Check if user was already registered
  - Use a different phone number or verify existing user via login

- **"User with username X already exists" on registration:**
  - Username must be unique across all users
  - Choose a different username
  - You can reuse the same phone number with different usernames (not recommended)

- **JWT token validation fails:**
  - Verify the token format: `Bearer <base64-encoded-token>`
  - Check for typos or truncation in the token
  - Token must start with `Bearer ` (with a space)
  - Ensure you're not missing the token or sending it in wrong header

- **Password not hashing correctly:**
  - All passwords are automatically hashed with BCrypt before storage
  - Never attempt to hash passwords in the client — send plain password and let server handle hashing
  - BCrypt includes automatic salting, so same password creates different hashes

### Auditing & CreatedBy/UpdatedBy Issues

- **createdBy/updatedBy showing "SYSTEM" instead of username:**
  - This occurs when requests are made without authentication (login first)
  - For authenticated requests, the username is automatically set from the JWT token
  - To customize auditor behavior, modify `MongoConfig#auditorAware()` bean

### Testing the API

**Quick test sequence using cURL:**

```bash
# 1. Register a user
curl -X POST http://localhost:8080/api/v1/users/add \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","phoneNumber":"9876543210","password":"Test@1234"}'

# 2. Login and get token (extract from response)
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/user/login \
  -H "Content-Type: application/json" \
  -d '{"phoneNumber":"9876543210","password":"Test@1234"}' \
  | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# 3. Add an expense (replace TOKEN with actual token)
curl -X POST http://localhost:8080/api/v1/expenses/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"userId":1001,"name":"Lunch","description":"Office lunch","amount":50.0}'

# 4. Get all expenses
curl -X GET http://localhost:8080/api/v1/expenses/all \
  -H "Authorization: Bearer $TOKEN"
```

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

**Completed:**
- ✅ User registration and authentication endpoints
- ✅ JWT-based security with token generation and validation
- ✅ BCrypt password encryption
- ✅ All expense CRUD endpoints (create, read by various filters, update status)
- ✅ Role-based access control (public vs. protected endpoints)
- ✅ Auditing with auditor awareness (authenticated user tracking)
- ✅ API documentation with Swagger/OpenAPI integration

**Future Enhancements:**
- Add pagination and sorting to expense list endpoints (GET /api/v1/expenses/all)
- Implement expense soft delete functionality
- Add expense approval workflow with multiple levels
- Implement expense categorization and tags
- Add expense reports and analytics endpoints
- Implement role-based authorization (@PreAuthorize, @PostAuthorize)
- Add integration tests for all endpoints
- Add unit tests for service layer
- Implement input validation annotations (@Valid, @NotBlank, etc.)
- Add rate limiting/throttling for API protection
- Implement refresh token mechanism (currently using 30-min expiring tokens)
- Add email notifications for expense approvals/rejections
- Implement audit trail/event logging for all operations
- Add expense export functionality (CSV, PDF)

---

## Contact / maintainers

Maintainership: Expenses Tracker Team (see repository authors)

If you need help understanding a specific class or behavior, open an issue or ask for a walkthrough of the relevant file(s).

---

Thank you for using the Expenses Tracker microservice!

