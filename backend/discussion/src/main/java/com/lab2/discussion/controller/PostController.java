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

import com.lab2.discussion.model.Post;
import com.lab2.discussion.repository.PostRepository;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired private PostRepository postRepo;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        post.setCreatedAt(new java.util.Date());
        return postRepo.save(post);
    }

    @GetMapping("/thread/{threadId}")
    public List<Post> getPostsByThread(@PathVariable String threadId) {
        return postRepo.findByThreadIdOrderByCreatedAtAsc(threadId);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable String id) {
        postRepo.deleteById(id);
        return "Post deleted.";
    }
}
