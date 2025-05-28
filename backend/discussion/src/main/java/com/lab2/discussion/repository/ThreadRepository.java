package com.lab2.discussion.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lab2.discussion.model.Thread;
@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
    // we can add custom queries later if needed
    List<Thread> findByTitleContainingIgnoreCase(String keyword);
}