package com.Assignment.adapter.repository;

import com.Assignment.domain.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
