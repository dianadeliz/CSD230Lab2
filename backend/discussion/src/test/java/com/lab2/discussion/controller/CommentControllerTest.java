package com.lab2.discussion.controller;

import com.lab2.discussion.config.TestConfig;
import com.lab2.discussion.config.TestSecurityConfig;
import com.lab2.discussion.model.Comment;
import com.lab2.discussion.model.Thread;
import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.CommentRepository;
import com.lab2.discussion.repository.ThreadRepository;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;
import com.lab2.discussion.service.CommentService;
import com.lab2.discussion.service.ThreadService;
import com.lab2.discussion.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@Import({TestConfig.class, TestSecurityConfig.class})
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ThreadService threadService;

    @MockBean
    private UserService userService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private ThreadRepository threadRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Thread testThread;
    private Comment testComment;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setRole("USER");

        testThread = new Thread();
        testThread.setTitle("Test Thread");
        testThread.setUsername("testuser");

        testComment = new Comment();
        testComment.setContent("Test Comment");
        testComment.setUsername("testuser");
        testComment.setThreadId("1");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(threadRepository.findById("1")).thenReturn(Optional.of(testThread));
        when(commentRepository.findById("1")).thenReturn(Optional.of(testComment));
        when(jwtUtil.validateToken(any())).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(any())).thenReturn("testuser");
        when(commentService.createComment(any(Comment.class))).thenReturn(testComment);
        when(commentService.updateComment(any(String.class), any(Comment.class))).thenReturn(testComment);
    }

    @Test
    public void testCommentController() throws Exception {
        // Test creating a comment
        mockMvc.perform(post("/api/comments")
                .header("Authorization", "Bearer test.jwt.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(testComment.getContent()))
                .andExpect(jsonPath("$.username").value(testComment.getUsername()));

        // Test getting comments by thread ID
        mockMvc.perform(get("/api/comments/thread/1")
                .header("Authorization", "Bearer test.jwt.token"))
                .andExpect(status().isOk());

        // Test updating a comment
        testComment.setContent("Updated Comment");
        mockMvc.perform(put("/api/comments/1")
                .header("Authorization", "Bearer test.jwt.token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(testComment.getContent()));

        // Test deleting a comment
        mockMvc.perform(delete("/api/comments/1")
                .header("Authorization", "Bearer test.jwt.token"))
                .andExpect(status().isOk());
    }
} 