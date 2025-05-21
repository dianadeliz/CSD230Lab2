# CSD230Lab2
Designing and implementing a Discussion Board using Bootstrap and Spring. 
Discussion Board Project
Technology Stack: Spring Boot, MongoDB, JWT, React, Bootstrap
<br>
<br>
Introduction
The Discussion Board project is a full-stack web application built with React and Bootstrap on the frontend, Spring Boot for the backend, and MongoDB for data storage. It allows users to register, log in, create discussion threads, and post replies. Admin users can delete posts and threads, provided the thread has no replies. The application uses JWT for authentication and authorization, ensuring secure access control.
<br>
System Architecture
•	Frontend: React, Bootstrap 5, Axios
•	Backend: Spring Boot with JWT security
•	Database: MongoDB (collections: users, threads, posts)
•	Authentication: JWT token stored in localStorage and validated by Spring Security middleware
<br>
Authentication and Authorization
•	Users register and login using credentials.
•	Backend generates a JWT token including the role (user/admin).
•	The token is used in requests and validated by JwtFilter.
•	Only users with admin role can delete posts/threads.
<br>
Main Features
•	Register and login with password encryption (BCrypt).
•	JWT token handling and session storage.
•	Threads and posts are displayed with timestamps.
•	Admin-only buttons for deleting threads and posts.
•	Responsive Bootstrap design with modals for thread/post creation and deletion.
<br>
Key Backend Components
Controllers: AuthController.java, ThreadController.java, PostController.java
Security: SecurityConfig.java, JwtUtil.java, JwtFilter.java
Models: User.java, Thread.java, Post.java
Repositories: UserRepository.java, ThreadRepository.java, PostRepository.java
Configuration: application.properties with MongoDB and JWT secret
<br>
Key Frontend Components
Pages: Login.js, Register.js, Threads.js, Posts.js
Components: Layout.js, Navbar.js, Layout.css
Utils: api.js with Axios interceptors for token handling
<br>
MongoDB Structure Compass View
users: stores registered users with hashed passwords and roles
threads: stores discussion threads with titles, usernames, and timestamps
posts: stores replies within threads with references to threadId
<br>
API Testing with Postman
Postman is used to test endpoints like `/register`, `/login`, `/threads`, and `/posts`. Authentication requires a Bearer Token in the Authorization header.
<br>
Admin-Only Functions
Admin users have additional permissions, such as deleting threads. Authorization is checked in controller methods based on the JWT token's role claim.
<br>
How to Execute
1. Start MongoDB locally.
2. Run the Spring Boot backend (port 8080) via `mvn spring-boot:run` or from your IDE.
3. Run the React frontend (port 3000) via `npm start`.
4. Register users from the UI or using Postman with a POST to /api/register.
5. Login through the UI and navigate between threads and posts.
<img width="1307" alt="loginadmin" src="https://github.com/user-attachments/assets/c2488316-e17f-4983-abba-132acb29fac5" />
