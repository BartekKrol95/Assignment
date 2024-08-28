package com.Assignment.controllerTests;

import static com.Assignment.utils.TestDataProvider.APOLLO_MISSIONS;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.request.RegisterProjectRequest;
import com.Assignment.domain.service.ProjectServiceImplementation;
import com.Assignment.utils.TestDataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectControllerTest {

    @MockBean
    private ProjectServiceImplementation projectServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFetchAllProjects_ValidPageable_ReturnsPagedProjects() throws Exception {
        // given
        List<ProjectDTO> projectDTOs = TestDataProvider.FAMOUS_PROJECT_DTOS;
        Pageable pageable = PageRequest.of(0, 2);
        Page<ProjectDTO> page = new PageImpl<>(projectDTOs, pageable, projectDTOs.size());

        when(projectServiceImpl.getAllProjects(pageable)).thenReturn(page);

        // when // then
        mockMvc.perform(get("/api/v1/projects?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.size", is(2)));

        verify(projectServiceImpl).getAllProjects(pageable);
    }

    @ParameterizedTest
    @MethodSource("provideRegisterProjectRequests")
    void testRegisterProject_WithValidAndInvalidInputs(String name, String description, String expectedErrorMessage) throws Exception {
        // given
        RegisterProjectRequest request = new RegisterProjectRequest();
        request.setName(name);
        request.setDescription(description);

        if (expectedErrorMessage == null) {
            // successful case
            ProjectDTO projectDto = new ProjectDTO();
            projectDto.setName(name);
            projectDto.setDescription(description);
            when(projectServiceImpl.createProject(any())).thenReturn(projectDto);

            // when // then
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is(name)))
                    .andExpect(jsonPath("$.description", is(description)));

            verify(projectServiceImpl).createProject(any());
        } else {
            // failure case
            mockMvc.perform(post("/api/v1/projects")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors[0].message").value(expectedErrorMessage));
        }
    }

    @Test
    void testGetProjectById_ValidId_ReturnsProject() throws Exception {
        // given
        Long id = 1L;
        ProjectDTO projectDto = APOLLO_MISSIONS;
        when(projectServiceImpl.getProjectById(id)).thenReturn(projectDto);

        // when // then
        mockMvc.perform(get("/api/v1/projects/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(projectDto.getName())))
                .andExpect(jsonPath("$.description", is(projectDto.getDescription())));

        verify(projectServiceImpl).getProjectById(id);
    }

    @Test
    void testModifyProject_ValidIdAndInput_UpdatesAndReturnsProject() throws Exception {
        // given
        Long id = 1L;
        ProjectDTO updatedProjectDto = new ProjectDTO();
        updatedProjectDto.setName("Updated Project");
        updatedProjectDto.setDescription("Updated Description");

        when(projectServiceImpl.updateProject(anyLong(), any())).thenReturn(updatedProjectDto);

        // when // then
        mockMvc.perform(put("/api/v1/projects/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Updated Project\", \"description\": \"Updated Description\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Project")))
                .andExpect(jsonPath("$.description", is("Updated Description")));

        verify(projectServiceImpl).updateProject(anyLong(), any());
    }

    static Stream<Arguments> provideRegisterProjectRequests() {
        return Stream.of(
                arguments("Gamma Project", "A detailed description of Gamma Project", null),
                arguments("invalidProjectName123", "A detailed description of Gamma Project", "PATTERN_MISMATCH_^[A-Z][a-z]{1,18}(?: [A-Z][a-z]{1,30})?$"),
                arguments("Gamma Project", "A".repeat(256), "DESCRIPTION_TOO_LONG")
        );
    }
}