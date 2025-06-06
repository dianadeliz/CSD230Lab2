package com.lab2.discussion.integration;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.lab2.discussion.config.SecurityConfig;
import com.lab2.discussion.config.TestBeansConfig;
import com.lab2.discussion.config.TestMongoConfig;
import com.lab2.discussion.model.Thread;
import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.ThreadRepository;
import com.lab2.discussion.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({SecurityConfig.class, TestMongoConfig.class, TestBeansConfig.class})
@ActiveProfiles("test")
public class AuthAndThreadIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        threadRepository.deleteAll();
    }

    @Test
    public void testLoginAndCreateThread() {
        // Register a user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole("USER");

        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
            getBaseUrl() + "/auth/register",
            user,
            Map.class
        );
        assert registerResponse.getStatusCode() == HttpStatus.OK;

        // Login
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password123");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
            getBaseUrl() + "/auth/login",
            loginRequest,
            Map.class
        );
        assert loginResponse.getStatusCode() == HttpStatus.OK;
        String token = (String) loginResponse.getBody().get("token");

        // Create thread
        Thread thread = new Thread();
        thread.setTitle("Test Thread");
        thread.setUsername("testuser");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Thread> threadRequest = new HttpEntity<>(thread, headers);

        ResponseEntity<Thread> threadResponse = restTemplate.exchange(
            getBaseUrl() + "/threads",
            HttpMethod.POST,
            threadRequest,
            Thread.class
        );
        assert threadResponse.getStatusCode() == HttpStatus.OK;
    }

    @Test
    public void testInvalidLogin() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "nonexistent");
        loginRequest.put("password", "wrongpassword");

        ResponseEntity<Map> response = restTemplate.postForEntity(
            getBaseUrl() + "/auth/login",
            loginRequest,
            Map.class
        );
        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
    }

    @Test
    public void testGetAllThreads() {
        // Register and login to get token
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole("USER");
        restTemplate.postForEntity(getBaseUrl() + "/auth/register", user, Map.class);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password123");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
            getBaseUrl() + "/auth/login",
            loginRequest,
            Map.class
        );
        assert loginResponse.getStatusCode() == HttpStatus.OK;
        String token = (String) loginResponse.getBody().get("token");

        // Get threads with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Thread[]> response = restTemplate.exchange(
            getBaseUrl() + "/threads",
            HttpMethod.GET,
            request,
            Thread[].class
        );
        assert response.getStatusCode() == HttpStatus.OK;
    }
} 