package com.lab2.discussion.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String username = "testuser";
        String role = "USER";
        String token = jwtUtil.generateToken(username, role);
        
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
        assertEquals(role, jwtUtil.getRoleFromToken(token));
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void getUsernameFromToken_ShouldExtractUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username, "USER");
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void getRoleFromToken_ShouldExtractRole() {
        String role = "ADMIN";
        String token = jwtUtil.generateToken("testuser", role);
        assertEquals(role, jwtUtil.getRoleFromToken(token));
    }
} 