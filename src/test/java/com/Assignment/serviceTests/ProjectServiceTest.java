package com.Assignment.serviceTests;

import static com.Assignment.utils.TestDataProvider.APOLLO_MISSIONS;
import static com.Assignment.utils.TestDataProvider.CERN_HADRON_COLLIDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.Assignment.adapter.repository.ProjectRepository;
import com.Assignment.api.exception.ProjectNotFoundException;
import com.Assignment.domain.mapper.ProjectMapper;
import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.Project;
import com.Assignment.domain.model.request.ModifyProjectRequest;
import com.Assignment.domain.model.request.RegisterProjectRequest;
import com.Assignment.domain.service.ProjectServiceImplementation;
import com.Assignment.utils.TestDataProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper demoProjectMapper;

    @InjectMocks
    private ProjectServiceImplementation projectService;

    @Test
    void testGetAllProjects_ValidPageable_ReturnsPagedProjects() {
        // given
        List<Project> projects = List.of(new Project(), new Project());
        Pageable pageable = PageRequest.of(0, 2);
        Page<Project> page = new PageImpl<>(projects, pageable, projects.size());

        when(projectRepository.findAll(pageable)).thenReturn(page);
        when(demoProjectMapper.mapToDto(projects.get(0))).thenReturn(APOLLO_MISSIONS);
        when(demoProjectMapper.mapToDto(projects.get(1))).thenReturn(CERN_HADRON_COLLIDER);

        // when
        Page<ProjectDTO> result = projectService.getAllProjects(pageable);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals(List.of(APOLLO_MISSIONS, CERN_HADRON_COLLIDER), result.getContent());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());
        verify(projectRepository).findAll(pageable);
    }

    @Test
    void testCreateProject_ValidInput_CreatesAndReturnsProject() {
        // given
        RegisterProjectRequest request = TestDataProvider.NASA_NEW_MISSION_REQUEST;
        Project project = new Project();
        when(demoProjectMapper.mapFromRequest(request)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(demoProjectMapper.mapToDto(project)).thenReturn(APOLLO_MISSIONS);

        // when
        ProjectDTO result = projectService.createProject(request);

        // then
        assertEquals(APOLLO_MISSIONS, result);
        verify(projectRepository).save(project);
    }

    @Test
    void testGetProjectById_ValidId_ReturnsProject() {
        // given
        Long id = 1L;
        Project project = new Project();
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(demoProjectMapper.mapToDto(project)).thenReturn(APOLLO_MISSIONS);

        // when
        ProjectDTO result = projectService.getProjectById(id);

        // then
        assertEquals(APOLLO_MISSIONS, result);
        verify(projectRepository).findById(id);
    }

    @Test
    void testGetProjectById_InvalidId_ThrowsEntityNotFoundException() {
        // given
        Long id = 1L;
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        // when then
        ProjectNotFoundException thrown = assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProjectById(id);
        });

        assertTrue(thrown.getMessage().contains("Project with id=" + id + " not found"));
        verify(projectRepository).findById(id);
    }

    @Test
    void testUpdateProject_ValidIdAndInput_UpdatesAndReturnsProject() {
        // given
        Long id = 1L;
        Project project = new Project();
        project.setVersion(1L);

        ModifyProjectRequest updateRequest = new ModifyProjectRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Description");

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(demoProjectMapper.mapToDto(project)).thenReturn(CERN_HADRON_COLLIDER);

        // when
        ProjectDTO result = projectService.updateProject(id, updateRequest);

        // then
        assertEquals(CERN_HADRON_COLLIDER, result);
        verify(projectRepository).findById(id);
        verify(projectRepository).save(project);
        assertEquals("New Name", project.getName());
        assertEquals("New Description", project.getDescription());
    }

    @Test
    void testUpdateProject_InvalidId_ThrowsEntityNotFoundException() {
        // given
        Long id = 1L;
        ModifyProjectRequest updateRequest = new ModifyProjectRequest();
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        // when then
        ProjectNotFoundException thrown = assertThrows(ProjectNotFoundException.class, () -> {
            projectService.updateProject(id, updateRequest);
        });

        assertTrue(thrown.getMessage().contains("Project with id=" + id + " not found"));
        verify(projectRepository).findById(id);
    }
}