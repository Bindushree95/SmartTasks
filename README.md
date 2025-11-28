<<<<<<< HEAD
# SmartTasks
SmartTasks - A Simple To-Do List Web Application with Spring Boot backend
=======
# SmartTasks API

A lightweight to-do list RESTful API built with Spring Boot 3.2 and Java 21.

## Features

- ✅ User Registration & Authentication (JWT-based)
- ✅ Task CRUD Operations (Create, Read, Update, Delete)
- ✅ Task Completion Toggle
- ✅ Task Filtering (All, Completed, Pending)
- ✅ Statistics Dashboard (Total, Completed, Pending counts)
- ✅ Password Encryption (BCrypt)
- ✅ Global Exception Handling
- ✅ Request Validation
- ✅ CORS Configuration
- ✅ Audit Logging

## Tech Stack

- **Java**: 21 LTS
- **Spring Boot**: 3.2.0
- **Database**: H2 (Development), PostgreSQL (Production)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven 3.9.11
- **ORM**: Spring Data JPA / Hibernate

## Prerequisites

- Java 21 or higher
- Maven 3.9.11 or higher

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd Activity
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

### 4. Access H2 Console (Development)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:smarttasks`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Authentication

#### Register a new user
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com"
}
```

### Tasks (Requires Authentication)

**Note:** Include the JWT token in all task requests:
```
Authorization: Bearer <your-token>
```

#### Create a new task
```http
POST /api/tasks
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Buy groceries",
  "description": "Milk, bread, eggs"
}
```

#### Get all tasks
```http
GET /api/tasks
Authorization: Bearer <token>

# Filter by completion status
GET /api/tasks?completed=true   # Get completed tasks
GET /api/tasks?completed=false  # Get pending tasks
```

#### Get a specific task
```http
GET /api/tasks/{id}
Authorization: Bearer <token>
```

#### Update a task
```http
PUT /api/tasks/{id}
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Updated title",
  "description": "Updated description"
}
```

#### Toggle task completion
```http
PATCH /api/tasks/{id}/toggle
Authorization: Bearer <token>
```

#### Delete a task
```http
DELETE /api/tasks/{id}
Authorization: Bearer <token>
```

### Statistics

#### Get task statistics
```http
GET /api/stats
Authorization: Bearer <token>
```

**Response:**
```json
{
  "totalTasks": 10,
  "completedTasks": 6,
  "pendingTasks": 4
}
```

## Configuration

### Application Properties

Key configurations in `application.yml`:

- **Server Port**: 8080
- **JWT Secret**: Configured in `jwt.secret`
- **JWT Expiration**: 24 hours (86400000 ms)
- **Database**: H2 in-memory (dev), PostgreSQL (prod)

### Production Configuration

For production, update `application-prod.yml` with PostgreSQL settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smarttasks
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

Run with production profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Error Handling

The API uses global exception handling and returns structured error responses:

```json
{
  "timestamp": "2025-11-27T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email is already registered",
  "path": "/api/auth/register"
}
```

## Security

- Passwords are hashed using BCrypt
- JWT tokens expire after 24 hours
- CSRF protection disabled for stateless API
- CORS configured for frontend integration
- All endpoints except `/api/auth/**` require authentication

## Logging

Application logs key actions:
- User registration and login
- Task CRUD operations
- Authentication failures
- Unexpected errors

Log level can be configured in `application.yml`

## Project Structure

```
src/main/java/com/smarttasks/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   └── JpaConfig.java
├── controller/          # REST controllers
│   ├── AuthController.java
│   ├── TaskController.java
│   └── StatsController.java
├── dto/                 # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── AuthResponse.java
│   ├── TaskRequest.java
│   ├── TaskResponse.java
│   ├── StatsResponse.java
│   └── ApiResponse.java
├── entity/              # JPA entities
│   ├── User.java
│   └── Task.java
├── exception/           # Custom exceptions & handler
│   ├── BadRequestException.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── repository/          # Data repositories
│   ├── UserRepository.java
│   └── TaskRepository.java
├── security/            # Security components
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenUtil.java
│   └── UserPrincipal.java
└── service/             # Business logic
    ├── AuthService.java
    ├── TaskService.java
    └── CustomUserDetailsService.java
```

## Testing

Run tests with:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is part of a training exercise for learning Spring Boot and REST API development.

## Author

SmartTasks Development Team
>>>>>>> e19ebf5 (Complete SmartTasks backend implementation with REST API)
