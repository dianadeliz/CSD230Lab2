package com.lab2.discussion.repository;

import com.lab2.discussion.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByThreadIdOrderByCreatedAtAsc(String threadId);
}