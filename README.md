# CSD230Lab2
Designing and implementing a Discussion Board using Bootstrap and Spring.
<br>
<br>
Discussion Board Project
<br>
Technology Stack: Spring Boot, MongoDB, JWT, React, Bootstrap
<br>
<br>
Introduction
The Discussion Board project is a full-stack web application built with React and Bootstrap on the frontend, Spring Boot for the backend, and MongoDB for data storage. It allows users to register, log in, create discussion threads, and post replies. Admin users can delete posts and threads, provided the thread has no replies. The application uses JWT for authentication and authorization, ensuring secure access control.
<br>
<br>
System Architecture
<br>
•	Frontend: React, Bootstrap 5, Axios
<br>
•	Backend: Spring Boot with JWT security
<br>
•	Database: MongoDB (collections: users, threads, posts)
<br>
•	Authentication: JWT token stored in localStorage and validated by Spring Security middleware
<br>
<br>
Authentication and Authorization
<br>
•	Users register and login using credentials.
<br>
•	Backend generates a JWT token including the role (user/admin).
<br>
•	The token is used in requests and validated by JwtFilter.
<br>
•	Only users with admin role can delete posts/threads.
<br>
<br>
Main Features
<br>
•	Register and login with password encryption (BCrypt).
<br>
•	JWT token handling and session storage.
<br>
•	Threads and posts are displayed with timestamps.
<br>
•	Admin-only buttons for deleting threads and posts.
<br>
•	Responsive Bootstrap design with modals for thread/post creation and deletion.
<br>
<br>
Key Backend Components
<br>
Controllers: AuthController.java, ThreadController.java, PostController.java
<br>
Security: SecurityConfig.java, JwtUtil.java, JwtFilter.java
<br>
Models: User.java, Thread.java, Post.java
<br>
Repositories: UserRepository.java, ThreadRepository.java, PostRepository.java
<br>
Configuration: application.properties with MongoDB and JWT secret
<br>
<br>
Key Frontend Components
<br>
Pages: Login.js, Register.js, Threads.js, Posts.js
<br>
Components: Layout.js, Navbar.js, Layout.css
<br>
Utils: api.js with Axios interceptors for token handling
<br>
<br>
MongoDB Structure Compass View
<br>
users: stores registered users with hashed passwords and roles
<br>
threads: stores discussion threads with titles, usernames, and timestamps
<br>
posts: stores replies within threads with references to threadId
<br>
<br>
API Testing with Postman
<br>
Postman is used to test endpoints like `/register`, `/login`, `/threads`, and `/posts`. Authentication requires a Bearer Token in the Authorization header.
<br>
Admin-Only Functions
<br>
Admin users have additional permissions, such as deleting threads. Authorization is checked in controller methods based on the JWT token's role claim.
<br>
<br>
How to Execute
1. Start MongoDB locally.
2. Run the Spring Boot backend (port 8080) via `mvn spring-boot:run` or from your IDE.
3. Run the React frontend (port 3000) via `npm start`.
4. Register users from the UI or using Postman with a POST to /api/register.
5. Login through the UI and navigate between threads and posts.
<img width="1307" alt="loginadmin" src="https://github.com/user-attachments/assets/c2488316-e17f-4983-abba-132acb29fac5" />
<br>
<br>
<img width="1302" alt="threads1" src="https://github.com/user-attachments/assets/59d24930-8476-454b-a218-4cdcce24504c" />
<br>
<br>
<img width="1251" alt="lab2posts" src="https://github.com/user-attachments/assets/888f142b-a008-4d27-81a5-04f569d94c26" />


