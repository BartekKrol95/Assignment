package com.Assignment.domain.service;

import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.request.ModifyProjectRequest;
import com.Assignment.domain.model.request.RegisterProjectRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProjectService {
    Page<ProjectDTO> getAllProjects(Pageable pageable);

    ProjectDTO createProject(RegisterProjectRequest request);

    ProjectDTO getProjectById(Long id);

    ProjectDTO updateProject(Long id, ModifyProjectRequest request);
}