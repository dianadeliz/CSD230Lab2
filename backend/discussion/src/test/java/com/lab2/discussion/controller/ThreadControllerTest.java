package com.lab2.discussion.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.lab2.discussion.model.Thread;
import com.lab2.discussion.repository.PostRepository;
import com.lab2.discussion.repository.ThreadRepository;

class ThreadControllerTest {

    @Mock
    private ThreadRepository threadRepo;

    @Mock
    private PostRepository postRepo;

    @InjectMocks
    private ThreadController threadController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createThread_ShouldSaveAndReturnThread() {
        Thread thread = new Thread();
        thread.setTitle("Test Thread");
        thread.setUsername("testuser");

        when(threadRepo.save(any(Thread.class))).thenReturn(thread);

        Thread result = threadController.createThread(thread);

        assertNotNull(result);
        assertEquals("Test Thread", result.getTitle());
        assertEquals("testuser", result.getUsername());
        assertNotNull(result.getCreatedAt());
        verify(threadRepo).save(any(Thread.class));
    }

    @Test
    void getAllThreads_ShouldReturnListOfThreads() {
        Thread thread1 = new Thread();
        thread1.setTitle("Thread 1");
        Thread thread2 = new Thread();
        thread2.setTitle("Thread 2");

        when(threadRepo.findAll()).thenReturn(Arrays.asList(thread1, thread2));

        List<Thread> result = threadController.getAllThreads();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(threadRepo).findAll();
    }

    @Test
    void deleteThread_ShouldDeleteWhenNoReplies() {
        String threadId = "123";
        when(postRepo.findByThreadIdOrderByCreatedAtAsc(threadId)).thenReturn(Arrays.asList());

        String result = threadController.deleteThread(threadId);

        assertEquals("Thread deleted.", result);
        verify(threadRepo).deleteById(threadId);
    }

    @Test
    void deleteThread_ShouldNotDeleteWhenHasReplies() {
        String threadId = "123";
        when(postRepo.findByThreadIdOrderByCreatedAtAsc(threadId))
            .thenReturn(Arrays.asList(new com.lab2.discussion.model.Post()));

        String result = threadController.deleteThread(threadId);

        assertEquals("Thread has replies and cannot be deleted.", result);
        verify(threadRepo, never()).deleteById(any());
    }
} 