package com.lab2.discussion.controller;

import com.lab2.discussion.model.Post;
import com.lab2.discussion.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository postRepository;

    @Test
    public void testGetPostsByThread() throws Exception {
        String threadId = "test-thread";
        List<Post> posts = Arrays.asList(
            new Post(threadId, "Test Content 1", "user1"),
            new Post(threadId, "Test Content 2", "user2")
        );

        when(postRepository.findByThreadIdOrderByCreatedAtAsc(threadId)).thenReturn(posts);

        mockMvc.perform(get("/api/posts/thread/" + threadId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test Content 1"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].content").value("Test Content 2"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }
} 