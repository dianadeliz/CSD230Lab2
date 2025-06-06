# Testing Guide for Discussion Board Application

This guide provides step-by-step instructions for running and understanding the tests for our discussion board application.

## Quick Start

1. Clone the repository
2. Set up the development environment
3. Run the tests

## Prerequisites

### Backend Requirements
- Java 17 or higher
- Maven
- MongoDB (running locally on default port 27017)

### Frontend Requirements
- Node.js 14 or higher
- npm or yarn

## Running the Tests

### Backend Tests

1. Start MongoDB:
```bash
mongod
```

2. Navigate to the backend directory:
```bash
cd backend/discussion
```

3. Run all backend tests:
```bash
mvn test
```

The tests will run and show results in the console. You can find detailed reports in `target/surefire-reports/`.

### Frontend Tests

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies (if not already done):
```bash
npm install
```

3. Run all frontend tests:
```bash
npm test
```

This will start Jest in watch mode. You can:
- Press `a` to run all tests
- Press `f` to run only failed tests
- Press `p` to filter by filename
- Press `t` to filter by test name
- Press `q` to quit

## Test Structure

### Backend Tests (`backend/discussion/src/test/java/com/lab2/discussion/`)

#### Controller Tests
- `PostControllerTest.java`: Tests post-related endpoints
  - GET /api/posts/thread/{threadId}
  - POST /api/posts
  - DELETE /api/posts/{id}

- `UserControllerTest.java`: Tests user authentication
  - POST /api/auth/register
  - POST /api/auth/login

### Frontend Tests (`frontend/src/`)

#### Component Tests
- `PostList.test.jsx`: Tests the posts list component
- `LoginForm.test.jsx`: Tests the login functionality
- `RegisterForm.test.jsx`: Tests user registration

#### API Tests
- `api.test.js`: Tests API utility functions

## Common Issues and Solutions

### Backend Issues

1. **MongoDB Connection Error**
   - Ensure MongoDB is running: `mongod`
   - Check MongoDB connection string in `application.properties`

2. **Test Failures**
   - Run `mvn clean test` to clear any cached test results
   - Check MongoDB is running and accessible
   - Verify all dependencies are installed: `mvn clean install`

### Frontend Issues

1. **Module Not Found**
   - Run `npm install` to install dependencies
   - Clear npm cache: `npm cache clean --force`

2. **Test Environment Issues**
   - Clear Jest cache: `npm test -- --clearCache`
   - Check Node.js version: `node -v`

## Best Practices

1. **Before Running Tests**
   - Ensure MongoDB is running
   - All dependencies are installed
   - No other services are using required ports

2. **Writing Tests**
   - Follow existing test patterns
   - Include both success and error cases
   - Mock external dependencies
   - Use meaningful test descriptions

3. **Maintaining Tests**
   - Update tests when changing features
   - Keep test data up to date
   - Document any special test requirements

## Getting Help

If you encounter issues:
1. Check the error messages in the console
2. Review the test reports in:
   - Backend: `backend/discussion/target/surefire-reports/`
   - Frontend: `frontend/coverage/`
3. Ensure all prerequisites are met
4. Check the application logs

## Additional Resources

- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Jest Documentation](https://jestjs.io/docs/getting-started)
- [React Testing Library](https://testing-library.com/docs/react-testing-library/intro/) 