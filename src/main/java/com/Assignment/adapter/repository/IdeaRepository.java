package com.Assignment.adapter.repository;

import com.Assignment.domain.model.Idea;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
}
