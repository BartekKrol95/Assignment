package com.Assignment.domain.service;

import com.Assignment.adapter.repository.CommentRepository;
import com.Assignment.adapter.repository.IdeaRepository;
import com.Assignment.adapter.repository.LikeRepository;
import com.Assignment.api.exception.IdeaNotFoundException;
import com.Assignment.domain.mapper.CommentMapper;
import com.Assignment.domain.mapper.IdeaMapper;
import com.Assignment.domain.model.Comment;
import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.Idea;
import com.Assignment.domain.model.UserLike;
import com.Assignment.domain.model.request.RegisterIdeaRequest;
import com.Assignment.domain.model.request.SubmitCommentRequest;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdeaServiceImplementation implements IdeaService {

    private final IdeaRepository ideaRepository;
    private final IdeaMapper ideaMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final LikeRepository likeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<IdeaDTO> fetchAllIdeas(Pageable pageable) {
        return ideaRepository.findAll(pageable)
                .map(ideaMapper::mapToDto);
    }

    @Override
    public IdeaDTO registerIdea(RegisterIdeaRequest request) {
        Idea idea = ideaRepository.save(ideaMapper.mapFromCommand(request));
        return ideaMapper.mapToDto(idea);
    }

    @Override
    @Transactional
    public IdeaDTO endorseIdea(Long ideaId) {
        Idea idea = getIdeaById(ideaId);

        UserLike like = new UserLike();
        like.setIdea(idea);
        like.setCreatedAt(LocalDateTime.now());
        likeRepository.save(like);

        idea.getLikes().add(like);
        Idea updatedIdea = ideaRepository.save(idea);

        return ideaMapper.mapToDto(updatedIdea);
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long ideaId, SubmitCommentRequest request) {
        Idea idea = getIdeaById(ideaId);
        Comment savedComment = createAndSaveComment(idea, request);

        idea.getComments().add(savedComment);
        ideaRepository.save(idea);

        return commentMapper.mapToDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> fetchCommentsByIdeaId(Long ideaId) {
        Idea idea = getIdeaById(ideaId);
        return idea.getComments().stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    private Idea getIdeaById(Long ideaId) {
        return ideaRepository.findById(ideaId)
                .orElseThrow(() -> new IdeaNotFoundException(MessageFormat.format("Idea with id={0} not found", ideaId)));
    }


    private Comment createAndSaveComment(Idea idea, SubmitCommentRequest request) {
        Comment comment = commentMapper.mapFromCommand(request);
        comment.setIdea(idea);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}