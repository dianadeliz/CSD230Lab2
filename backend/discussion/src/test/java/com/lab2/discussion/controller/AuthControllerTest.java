package com.lab2.discussion.controller;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
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
        
        when(encoder.encode(any())).thenReturn("encodedPassword");
        
        String result = authController.register(user);
        
        assertEquals("User registered", result);
        verify(userRepo).save(any(User.class));
    }

    @Test
    void register_ShouldSaveUserWithProvidedRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ADMIN");
        
        when(encoder.encode(any())).thenReturn("encodedPassword");
        
        String result = authController.register(user);
        
        assertEquals("User registered", result);
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

        Map<String, String> response = authController.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt.token.here", response.get("token"));
        assertEquals("testuser", response.get("username"));
        assertEquals("USER", response.get("role"));
    }

    @Test
    void login_ShouldThrowExceptionForInvalidCredentials() {
        User loginRequest = new User();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        User storedUser = new User();
        storedUser.setUsername("testuser");
        storedUser.setPassword("encodedPassword");

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(storedUser));
        when(encoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
    }
} 