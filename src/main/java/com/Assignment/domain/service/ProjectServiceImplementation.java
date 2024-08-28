package com.Assignment.domain.service;

import com.Assignment.adapter.repository.ProjectRepository;
import com.Assignment.api.exception.ProjectNotFoundException;
import com.Assignment.domain.mapper.ProjectMapper;
import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.Project;
import com.Assignment.domain.model.request.ModifyProjectRequest;
import com.Assignment.domain.model.request.RegisterProjectRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImplementation implements ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::mapToDto);
    }

    @Override
    public ProjectDTO createProject(RegisterProjectRequest request) {
        Project demoProject = projectRepository.save(projectMapper.mapFromRequest(request));
        return projectMapper.mapToDto(demoProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        Project demoProject = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(MessageFormat.format("Project with id={0} not found", id)));
        return projectMapper.mapToDto(demoProject);
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long id, ModifyProjectRequest request) {
        Project project = getProjectByIdInternal(id);
        updateProjectDetails(project, request);
        return projectMapper.mapToDto(projectRepository.save(project));
    }

    private Project getProjectByIdInternal(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(MessageFormat.format("Project with id={0} not found", id)));
    }

    private void updateProjectDetails(Project project, ModifyProjectRequest request) {
        project.setName(request.getName());
        project.setDescription(request.getDescription());
    }
}