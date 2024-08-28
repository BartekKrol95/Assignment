package com.Assignment.domain.service;

import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.request.RegisterIdeaRequest;
import com.Assignment.domain.model.request.SubmitCommentRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IdeaService {
    Page<IdeaDTO> fetchAllIdeas(Pageable pageable);

    IdeaDTO registerIdea(RegisterIdeaRequest request);

    IdeaDTO endorseIdea(Long ideaId);

    CommentDTO addComment(Long ideaId, SubmitCommentRequest request);
    List<CommentDTO> fetchCommentsByIdeaId(Long ideaId);
}