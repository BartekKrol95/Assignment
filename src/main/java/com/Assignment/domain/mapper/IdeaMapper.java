package com.Assignment.domain.mapper;

import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.Idea;
import com.Assignment.domain.model.request.RegisterIdeaRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdeaMapper {

    @Mapping(target = "id", ignore = true)
    Idea mapFromCommand(RegisterIdeaRequest request);

    @Mapping(target = "likesCount", expression = "java(idea.getLikes().size())")
    IdeaDTO mapToDto(Idea idea);
}