# TailPair - Animal Adoption Platform Backend

A comprehensive Spring Boot backend for an animal adoption platform that connects potential adopters with animal shelters.

## Features

- **User Management**: Registration, authentication, and role-based access control
- **Animal Management**: CRUD operations for animals with advanced search and filtering
- **Shelter Management**: Shelter registration, verification, and management
- **Adoption Process**: Complete adoption workflow from request to completion
- **Messaging System**: Communication between adopters and shelters
- **Favorites**: Users can favorite animals they're interested in
- **Notifications**: Real-time notifications for important events
- **Security**: JWT-based authentication with role-based authorization

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Maven** for dependency management
- **Bean Validation** for input validation

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE tailpair;
CREATE USER tailpair WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE tailpair TO tailpair;
```

### 2. Environment Variables

Set the following environment variables or update `application.yml`:

```bash
export DB_USERNAME=tailpair
export DB_PASSWORD=password
export JWT_SECRET=mySecretKey
export CORS_ORIGINS=http://localhost:3000,http://localhost:5173
```

### 3. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd tailpair-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### User Endpoints

- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users` - Get all users (Admin only)

### Animal Endpoints

- `GET /api/animals/available` - Get available animals
- `GET /api/animals/{id}` - Get animal by ID
- `POST /api/animals` - Create new animal (Shelter Admin)
- `PUT /api/animals/{id}` - Update animal (Shelter Admin)
- `DELETE /api/animals/{id}` - Delete animal (Shelter Admin)
- `GET /api/animals/search?q={query}` - Search animals
- `GET /api/animals/filter` - Filter animals with multiple criteria

### Shelter Endpoints

- `GET /api/shelters` - Get all shelters
- `GET /api/shelters/{id}` - Get shelter by ID
- `POST /api/shelters` - Create new shelter (Shelter Admin)
- `PUT /api/shelters/{id}` - Update shelter (Shelter Admin)
- `GET /api/shelters/verified` - Get verified shelters

### Adoption Endpoints

- `POST /api/adoptions` - Create adoption request
- `GET /api/adoptions/user` - Get user's adoptions
- `PUT /api/adoptions/{id}/approve` - Approve adoption (Shelter Admin)
- `PUT /api/adoptions/{id}/reject` - Reject adoption (Shelter Admin)
- `PUT /api/adoptions/{id}/complete` - Complete adoption (Shelter Admin)

### Message Endpoints

- `POST /api/messages` - Send message
- `GET /api/messages/received` - Get received messages
- `GET /api/messages/sent` - Get sent messages
- `PUT /api/messages/{id}/read` - Mark message as read

### Favorite Endpoints

- `POST /api/favorites/animal/{animalId}` - Add to favorites
- `DELETE /api/favorites/animal/{animalId}` - Remove from favorites
- `GET /api/favorites` - Get user's favorite animals

### Notification Endpoints

- `GET /api/notifications/user` - Get user notifications
- `GET /api/notifications/unread` - Get unread notifications
- `PUT /api/notifications/{id}/read` - Mark notification as read

## User Roles

1. **ADOPTER** - Regular users who can browse and adopt animals
2. **SHELTER_ADMIN** - Shelter administrators who can manage animals and adoptions
3. **ADMIN** - System administrators with full access

## Database Schema

The application uses the following main entities:

- **User** - User accounts with role-based access
- **Shelter** - Animal shelters with verification status
- **Animal** - Animals available for adoption
- **Adoption** - Adoption requests and their status
- **Message** - Communication between users
- **Notification** - System notifications
- **Favorite** - User's favorite animals

## Security

- JWT-based authentication
- Role-based authorization using Spring Security
- Password encryption using BCrypt
- CORS configuration for frontend integration

## Configuration

Key configuration properties in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tailpair
    username: ${DB_USERNAME:tailpair}
    password: ${DB_PASSWORD:password}
  
  jpa:
    hibernate:
      ddl-auto: update

app:
  jwt:
    secret: ${JWT_SECRET:mySecretKey}
    expiration: 86400000 # 24 hours
  
  cors:
    allowed-origins: ${CORS_ORIGINS:http://localhost:3000}
```

## Testing

Run tests with:

```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License.