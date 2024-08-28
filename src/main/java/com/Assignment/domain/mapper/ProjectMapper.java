package com.Assignment.domain.mapper;

import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.Project;
import com.Assignment.domain.model.request.RegisterProjectRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "id", ignore = true)
    Project mapFromRequest(RegisterProjectRequest request);

    ProjectDTO mapToDto(Project project);
}