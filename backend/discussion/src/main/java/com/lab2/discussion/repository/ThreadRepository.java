package com.lab2.discussion.repository;

import com.lab2.discussion.model.Thread;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreadRepository extends MongoRepository<Thread, String> {
    // we can add custom queries later if needed
}