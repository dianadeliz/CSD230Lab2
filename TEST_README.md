# Testing Documentation

This document explains how to run and understand the tests for both the backend and frontend components of the discussion board application.

## Backend Tests (Spring Boot)

### Prerequisites
- Java 17 or higher
- Maven
- MongoDB (for integration tests)

### Running Backend Tests

1. Navigate to the backend directory:
```bash
cd backend/discussion
```

2. Run all tests:
```bash
mvn test
```

### Test Structure

The backend tests are located in `src/test/java/com/lab2/discussion/` and include:

#### Controller Tests
- `PostControllerTest.java`: Tests for post-related endpoints
  - Tests retrieving posts by thread
  - Tests post creation and deletion
  - Uses MockMvc for simulating HTTP requests

- `UserControllerTest.java`: Tests for user authentication
  - Tests user registration
  - Tests user login
  - Tests JWT token generation
  - Mocks BCrypt password encoding

### Test Reports
After running the tests, you can find the test reports in:
- `target/surefire-reports/` - Detailed test execution reports
- Console output - Summary of test results

## Frontend Tests (React)

### Prerequisites
- Node.js 14 or higher
- npm or yarn

### Running Frontend Tests

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Run all tests:
```bash
npm test
```

### Test Structure

The frontend tests are located in:
- `src/components/__tests__/` - Component tests
- `src/__tests__/` - Utility tests

#### Component Tests
- `PostList.test.jsx`: Tests for the posts list component
  - Tests rendering of posts
  - Tests post creation
  - Tests post deletion (admin only)
  - Mocks API calls and localStorage

- `LoginForm.test.jsx`: Tests for the login form
  - Tests form rendering
  - Tests successful login
  - Tests error handling
  - Verifies localStorage updates

- `RegisterForm.test.jsx`: Tests for the registration form
  - Tests form rendering
  - Tests successful registration
  - Tests error handling
  - Verifies API calls

#### API Tests
- `api.test.js`: Tests for the API utility
  - Tests GET requests
  - Tests POST requests
  - Tests DELETE requests
  - Tests error handling
  - Verifies authentication headers

### Test Commands

When running `npm test`, you'll enter Jest's interactive mode with the following options:
- Press `a` to run all tests
- Press `f` to run only failed tests
- Press `p` to filter by a filename regex pattern
- Press `t` to filter by a test name regex pattern
- Press `q` to quit watch mode

To run specific test files:
```bash
npm test PostList.test.jsx
npm test LoginForm.test.jsx
npm test RegisterForm.test.jsx
npm test api.test.js
```

### Test Coverage

To generate a test coverage report:
```bash
npm test -- --coverage
```

This will create a coverage report in the `coverage` directory, showing:
- Statement coverage
- Branch coverage
- Function coverage
- Line coverage

## Best Practices

1. **Backend Testing**
   - Use `@WebMvcTest` for controller tests
   - Mock repositories and services
   - Test both success and error scenarios
   - Verify response status and content

2. **Frontend Testing**
   - Use React Testing Library for component tests
   - Mock external dependencies (API, localStorage)
   - Test user interactions
   - Verify component rendering
   - Test error handling

3. **General Guidelines**
   - Write tests before implementing features (TDD)
   - Keep tests focused and isolated
   - Use meaningful test descriptions
   - Maintain test data separately
   - Regular test maintenance

## Troubleshooting

### Common Issues

1. **Backend Tests**
   - MongoDB connection issues: Ensure MongoDB is running
   - Port conflicts: Check if port 8080 is available
   - Maven dependency issues: Run `mvn clean install`

2. **Frontend Tests**
   - Module not found: Run `npm install`
   - Test environment issues: Clear Jest cache with `npm test -- --clearCache`
   - React version mismatch: Check package.json for correct versions

### Getting Help

If you encounter issues:
1. Check the test output for specific error messages
2. Review the test reports in the respective directories
3. Ensure all dependencies are correctly installed
4. Verify the test environment setup 