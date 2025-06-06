package com.lab2.discussion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lab2.discussion.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByThreadId(String threadId);
    List<Comment> findByUsername(String username);
} 