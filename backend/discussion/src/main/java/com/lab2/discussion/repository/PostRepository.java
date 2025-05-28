package com.lab2.discussion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lab2.discussion.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByThreadIdOrderByCreatedAtAsc(String threadId);
    List<Post> findByContentContainingIgnoreCase(String keyword);
}