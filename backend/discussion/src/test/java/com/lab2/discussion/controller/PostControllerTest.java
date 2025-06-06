package com.lab2.discussion.controller;

import com.lab2.discussion.config.TestMongoConfig;
import com.lab2.discussion.config.TestBeansConfig;
import com.lab2.discussion.config.TestSecurityConfig;
import com.lab2.discussion.model.Post;
import com.lab2.discussion.repository.PostRepository;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@Import({TestMongoConfig.class, TestBeansConfig.class, TestSecurityConfig.class})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testGetPostsByThread() throws Exception {
        String threadId = "123";
        Post post1 = new Post(threadId, "Test post 1", "user1");
        post1.setId("1");
        post1.setCreatedAt(new Date());
        
        Post post2 = new Post(threadId, "Test post 2", "user2");
        post2.setId("2");
        post2.setCreatedAt(new Date());

        when(postRepository.findByThreadIdOrderByCreatedAtAsc(threadId))
            .thenReturn(Arrays.asList(post1, post2));
        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(jwtUtil.getUsernameFromToken("valid-token")).thenReturn("testuser");
        when(jwtUtil.getRoleFromToken("valid-token")).thenReturn("USER");

        mockMvc.perform(get("/api/posts/thread/" + threadId)
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test post 1"))
                .andExpect(jsonPath("$[1].content").value("Test post 2"));
    }
} 