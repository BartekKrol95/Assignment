package com.Assignment.adapter.repository;

import com.Assignment.domain.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
