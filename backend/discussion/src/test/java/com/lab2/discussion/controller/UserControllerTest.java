package com.lab2.discussion.controller;

import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    public void testRegister() throws Exception {
        User user = new User("testuser", "password", "USER");
        when(encoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\",\"role\":\"USER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User("testuser", "encodedPassword", "USER");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(encoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("test-token");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
} 