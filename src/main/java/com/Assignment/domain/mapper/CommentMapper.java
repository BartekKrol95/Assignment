package com.Assignment.domain.mapper;

import com.Assignment.domain.model.Comment;
import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.request.SubmitCommentRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    Comment mapFromCommand(SubmitCommentRequest request);

    CommentDTO mapToDto(Comment request);
}
