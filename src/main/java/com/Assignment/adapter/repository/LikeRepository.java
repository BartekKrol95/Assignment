package com.Assignment.adapter.repository;

import com.Assignment.domain.model.UserLike;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<UserLike, Long> {
}
