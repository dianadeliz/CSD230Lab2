package com.lab2.discussion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab2.discussion.config.TestBeansConfig;
import com.lab2.discussion.config.TestMongoConfig;
import com.lab2.discussion.config.TestSecurityConfig;
import com.lab2.discussion.dto.LoginRequest;
import com.lab2.discussion.dto.RegisterRequest;
import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;

import java.util.Optional;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {
    UserController.class,
    TestBeansConfig.class,
    TestMongoConfig.class,
    TestSecurityConfig.class
})
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

} 