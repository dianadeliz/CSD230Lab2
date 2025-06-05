package com.lab2.discussion.integration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lab2.discussion.model.Thread;
import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.ThreadRepository;
import com.lab2.discussion.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthAndThreadIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String baseUrl;
    private String token;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
        
        // Clean up test data
        userRepository.deleteAll();
        threadRepository.deleteAll();

        // Create test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("USER");
        userRepository.save(user);

        // Login to get token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User loginRequest = new User();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        HttpEntity<User> loginEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
            baseUrl + "/login",
            loginEntity,
            Map.class
        );

        token = (String) loginResponse.getBody().get("token");
    }

    @Test
    void testLoginAndCreateThread() {
        // Set up headers with JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        // Create a new thread
        Thread thread = new Thread();
        thread.setTitle("Test Thread");
        thread.setUsername("testuser");

        HttpEntity<Thread> threadEntity = new HttpEntity<>(thread, headers);
        ResponseEntity<Thread> response = restTemplate.postForEntity(
            baseUrl + "/threads",
            threadEntity,
            Thread.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Thread", response.getBody().getTitle());
    }

    @Test
    void testGetAllThreads() {
        // Create some test threads
        Thread thread1 = new Thread();
        thread1.setTitle("Thread 1");
        thread1.setUsername("testuser");
        threadRepository.save(thread1);

        Thread thread2 = new Thread();
        thread2.setTitle("Thread 2");
        thread2.setUsername("testuser");
        threadRepository.save(thread2);

        // Set up headers with JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(
            baseUrl + "/threads",
            HttpMethod.GET,
            entity,
            List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() >= 2);
    }

    @Test
    void testInvalidLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User loginRequest = new User();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        HttpEntity<User> loginEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/login",
            loginEntity,
            Map.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
} 