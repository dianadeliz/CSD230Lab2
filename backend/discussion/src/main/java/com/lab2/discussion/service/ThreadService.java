package com.lab2.discussion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab2.discussion.model.Thread;
import com.lab2.discussion.repository.ThreadRepository;
import com.lab2.discussion.repository.UserRepository;

@Service
public class ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    public Thread createThread(Thread thread) {
        if (userRepository.findByUsername(thread.getUsername()).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return threadRepository.save(thread);
    }

    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    public Optional<Thread> getThreadById(String id) {
        return threadRepository.findById(id);
    }

    public Thread updateThread(String id, Thread thread) {
        Optional<Thread> existingThread = threadRepository.findById(id);
        if (existingThread.isEmpty()) {
            throw new RuntimeException("Thread not found");
        }
        Thread updatedThread = existingThread.get();
        updatedThread.setTitle(thread.getTitle());
        return threadRepository.save(updatedThread);
    }

    public void deleteThread(String id) {
        if (threadRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Thread not found");
        }
        threadRepository.deleteById(id);
    }
} 