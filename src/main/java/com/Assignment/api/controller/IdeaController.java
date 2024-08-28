package com.Assignment.api.controller;

import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.request.RegisterIdeaRequest;
import com.Assignment.domain.model.request.SubmitCommentRequest;
import com.Assignment.domain.service.IdeaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/projects/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @GetMapping
    public ResponseEntity<Page<IdeaDTO>> fetchAllIdeas(Pageable pageable) {
        Page<IdeaDTO> ideas = ideaService.fetchAllIdeas(pageable);
        return ResponseEntity.ok(ideas);
    }

    @PostMapping
    public ResponseEntity<IdeaDTO> registerIdea(@RequestBody @Valid RegisterIdeaRequest request) {
        IdeaDTO newIdea = ideaService.registerIdea(request);
        return new ResponseEntity<>(newIdea, HttpStatus.CREATED);
    }

    @PostMapping("/{ideaId}/like")
    public ResponseEntity<IdeaDTO> endorseIdea(@PathVariable("ideaId") Long id) {
        IdeaDTO updatedIdea = ideaService.endorseIdea(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedIdea);
    }

    @PostMapping("/{ideaId}/comments")
    public ResponseEntity<CommentDTO> submitComment(
            @PathVariable("ideaId") Long id,
            @RequestBody SubmitCommentRequest request) {
        CommentDTO newComment = ideaService.addComment(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }

    @GetMapping("/{ideaId}/comments")
    public ResponseEntity<List<CommentDTO>> fetchCommentsByIdeaId(@PathVariable("ideaId") Long ideaId) {
        List<CommentDTO> comments = ideaService.fetchCommentsByIdeaId(ideaId);
        return ResponseEntity.ok(comments);
    }
}