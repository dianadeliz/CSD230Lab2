package com.lab2.discussion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab2.discussion.model.Thread;
import com.lab2.discussion.repository.PostRepository;
import com.lab2.discussion.repository.ThreadRepository;

@RestController
@RequestMapping("/api/threads")
public class ThreadController {

    @Autowired private ThreadRepository threadRepo;
    @Autowired private PostRepository postRepo;

    @PostMapping
    public Thread createThread(@RequestBody Thread thread) {
        thread.setCreatedAt(new java.util.Date());
        return threadRepo.save(thread);
    }

    @GetMapping
    public List<Thread> getAllThreads() {
        return threadRepo.findAll();
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public String deleteThread(@PathVariable String id) {
        boolean hasReplies = !postRepo.findByThreadIdOrderByCreatedAtAsc(id).isEmpty();
        if (hasReplies) {
            return "Thread has replies and cannot be deleted.";
        }
        threadRepo.deleteById(id);
        return "Thread deleted.";
    }
}
