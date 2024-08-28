package com.Assignment.api.controller;

import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.request.ModifyProjectRequest;
import com.Assignment.domain.model.request.RegisterProjectRequest;
import com.Assignment.domain.service.ProjectService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> fetchAllProjects(Pageable pageable) {
        Page<ProjectDTO> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> registerProject(@RequestBody @Valid RegisterProjectRequest request) {
        ProjectDTO newProject = projectService.createProject(request);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable("id") Long projectId) {
        ProjectDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> modifyProject(
            @PathVariable("id") Long projectId,
            @RequestBody ModifyProjectRequest updateRequest) {
        ProjectDTO updatedProject = projectService.updateProject(projectId, updateRequest);
        return ResponseEntity.ok(updatedProject);
    }
}