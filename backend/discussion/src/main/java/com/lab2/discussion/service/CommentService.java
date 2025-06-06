package com.lab2.discussion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lab2.discussion.model.Comment;
import com.lab2.discussion.repository.CommentRepository;
import com.lab2.discussion.repository.ThreadRepository;
import com.lab2.discussion.repository.UserRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment createComment(Comment comment) {
        if (threadRepository.findById(comment.getThreadId()).isEmpty()) {
            throw new RuntimeException("Thread not found");
        }
        if (userRepository.findByUsername(comment.getUsername()).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByThreadId(String threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    public Comment updateComment(String id, Comment comment) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        Comment updatedComment = existingComment.get();
        updatedComment.setContent(comment.getContent());
        return commentRepository.save(updatedComment);
    }

    public void deleteComment(String id) {
        if (commentRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
} 