package com.lab2.discussion.controller;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;

class AuthControllerTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_ShouldSaveUserWithDefaultRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("USER");
        
        when(encoder.encode(any())).thenReturn("encodedPassword");
        
        Map<String, String> result = authController.register(user);
        
        assertEquals("User registered", result.get("message"));
        assertEquals("testuser", result.get("username"));
        assertEquals("test@example.com", result.get("email"));
        assertEquals("USER", result.get("role"));
        verify(userRepo).save(any(User.class));
    }

    @Test
    void register_ShouldSaveUserWithProvidedRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole("ADMIN");
        
        when(encoder.encode(any())).thenReturn("encodedPassword");
        
        Map<String, String> result = authController.register(user);
        
        assertEquals("User registered", result.get("message"));
        assertEquals("testuser", result.get("username"));
        assertEquals("test@example.com", result.get("email"));
        assertEquals("ADMIN", result.get("role"));
        verify(userRepo).save(any(User.class));
    }

    @Test
    void login_ShouldReturnTokenForValidCredentials() {
        User loginRequest = new User();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        User storedUser = new User();
        storedUser.setUsername("testuser");
        storedUser.setPassword("encodedPassword");
        storedUser.setRole("USER");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(storedUser));
        when(encoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("jwt.token.here");

        ResponseEntity<?> response = authController.login(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("jwt.token.here", body.get("token"));
        assertEquals("testuser", body.get("username"));
        assertEquals("USER", body.get("role"));
    }

    @Test
    void login_ShouldReturnForbiddenForInvalidCredentials() {
        User loginRequest = new User();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        User storedUser = new User();
        storedUser.setUsername("testuser");
        storedUser.setPassword("encodedPassword");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(storedUser));
        when(encoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = authController.login(loginRequest);
        assertEquals(403, response.getStatusCodeValue());
        Map<String, String> body = (Map<String, String>) response.getBody();
        assertNotNull(body);
        assertEquals("Invalid credentials", body.get("error"));
    }
} 