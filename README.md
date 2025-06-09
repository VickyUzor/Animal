# TailPair - Animal Adoption Platform

A comprehensive full-stack application for animal adoption that connects potential adopters with animal shelters.

## Features

### Frontend (React + TypeScript)
- **Modern UI/UX**: Clean, responsive design with Tailwind CSS
- **Animal Discovery**: Browse, search, and filter animals by various criteria
- **User Authentication**: Secure login/registration with role-based access
- **Favorites System**: Save favorite animals for later viewing
- **Adoption Requests**: Submit and track adoption applications
- **Messaging**: Communicate with shelters about animals
- **Notifications**: Real-time updates on adoption status
- **Responsive Design**: Works seamlessly on desktop and mobile

### Backend (Spring Boot + Java)
- **RESTful API**: Comprehensive API for all frontend operations
- **JWT Authentication**: Secure token-based authentication
- **Role-based Authorization**: Different access levels for adopters, shelter admins, and system admins
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **Advanced Search**: Full-text search and filtering capabilities
- **Email Notifications**: Automated notifications for important events
- **Data Validation**: Comprehensive input validation and error handling

## Technology Stack

### Frontend
- **React 18** with TypeScript
- **Vite** for fast development and building
- **Tailwind CSS** for styling
- **React Router** for navigation
- **Axios** for API communication
- **React Query** for data fetching and caching
- **React Hook Form** for form handling
- **React Hot Toast** for notifications
- **Lucide React** for icons

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **PostgreSQL** database
- **Maven** for dependency management
- **Bean Validation** for input validation

## Getting Started

### Prerequisites
- Node.js 18+ and npm
- Java 17+
- PostgreSQL 12+
- Maven 3.6+

### Backend Setup

1. **Database Setup**
   ```sql
   CREATE DATABASE tailpair;
   CREATE USER tailpair WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE tailpair TO tailpair;
   ```

2. **Environment Variables**
   ```bash
   export DB_USERNAME=tailpair
   export DB_PASSWORD=password
   export JWT_SECRET=mySecretKey
   export CORS_ORIGINS=http://localhost:5173
   ```

3. **Run Backend**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:5173`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Animals
- `GET /api/animals/available` - Get available animals
- `GET /api/animals/{id}` - Get animal details
- `GET /api/animals/search` - Search animals
- `GET /api/animals/filter` - Filter animals
- `POST /api/animals` - Create animal (Shelter Admin)
- `PUT /api/animals/{id}` - Update animal (Shelter Admin)

### Adoptions
- `POST /api/adoptions` - Create adoption request
- `GET /api/adoptions/user` - Get user's adoptions
- `PUT /api/adoptions/{id}/approve` - Approve adoption (Shelter Admin)
- `PUT /api/adoptions/{id}/reject` - Reject adoption (Shelter Admin)

### Favorites
- `POST /api/favorites/animal/{animalId}` - Add to favorites
- `DELETE /api/favorites/animal/{animalId}` - Remove from favorites
- `GET /api/favorites` - Get favorite animals

### Messages & Notifications
- `POST /api/messages` - Send message
- `GET /api/messages/received` - Get received messages
- `GET /api/notifications/user` - Get user notifications

## User Roles

1. **ADOPTER** - Regular users who can browse and adopt animals
2. **SHELTER_ADMIN** - Shelter administrators who can manage animals and adoptions
3. **ADMIN** - System administrators with full access

## Features in Detail

### Animal Management
- Comprehensive animal profiles with photos, descriptions, and medical history
- Advanced filtering by species, breed, age, size, and characteristics
- Real-time availability status updates

### Adoption Process
- Simple adoption request submission
- Shelter admin approval workflow
- Status tracking and notifications
- Communication between adopters and shelters

### User Experience
- Intuitive navigation and search
- Mobile-responsive design
- Real-time notifications
- Favorites and saved searches

## Development

### Frontend Development
```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run lint         # Run ESLint
npm run preview      # Preview production build
```

### Backend Development
```bash
mvn spring-boot:run  # Start development server
mvn test            # Run tests
mvn clean install   # Build project
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

For support, email contact@tailpair.com or create an issue in the repository.