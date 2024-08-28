package com.Assignment.serviceTests;

import static com.Assignment.utils.TestDataProvider.ADVANCED_PROPULSION_IDEA_REQUEST;
import static com.Assignment.utils.TestDataProvider.ENTHUSIASTIC_COMMENT_REQUEST;
import static com.Assignment.utils.TestDataProvider.FUSION_ENERGY;
import static com.Assignment.utils.TestDataProvider.QUANTUM_COMPUTING;
import static com.Assignment.utils.TestDataProvider.SPACE_ELEVATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.Assignment.domain.service.IdeaServiceImplementation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class IdeaServiceTest {

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private IdeaMapper ideaMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private IdeaServiceImplementation ideaService;

    @Test
    void testFetchAllIdeas_ValidPageable_ReturnsPagedIdeas() {
        // given
        List<Idea> ideas = List.of(new Idea(), new Idea());
        Pageable pageable = PageRequest.of(0, 2);
        Page<Idea> page = new PageImpl<>(ideas, pageable, ideas.size());

        when(ideaRepository.findAll(pageable)).thenReturn(page);
        when(ideaMapper.mapToDto(ideas.get(0))).thenReturn(SPACE_ELEVATOR);
        when(ideaMapper.mapToDto(ideas.get(1))).thenReturn(QUANTUM_COMPUTING);

        // when
        Page<IdeaDTO> result = ideaService.fetchAllIdeas(pageable);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals(List.of(SPACE_ELEVATOR, QUANTUM_COMPUTING), result.getContent());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());
        verify(ideaRepository).findAll(pageable);
    }

    @Test
    void testRegisterIdea_ValidInput_CreatesAndReturnsIdea() {
        // given
        RegisterIdeaRequest request = ADVANCED_PROPULSION_IDEA_REQUEST;
        Idea idea = new Idea();
        when(ideaMapper.mapFromCommand(request)).thenReturn(idea);
        when(ideaRepository.save(idea)).thenReturn(idea);
        when(ideaMapper.mapToDto(idea)).thenReturn(FUSION_ENERGY);

        // when
        IdeaDTO result = ideaService.registerIdea(request);

        // then
        assertEquals(FUSION_ENERGY, result);
        verify(ideaRepository).save(idea);
    }

    @Test
    void testEndorseIdea_ValidId_CreatesLikeAndReturnsIdea() {
        // given
        Long ideaId = 1L;
        Idea idea = new Idea();
        idea.setId(ideaId);
        idea.setTitle(SPACE_ELEVATOR.getTitle());
        idea.setDescription(SPACE_ELEVATOR.getDescription());
        idea.setAuthor(SPACE_ELEVATOR.getAuthor());
        idea.setLikes(new ArrayList<>());

        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));
        when(likeRepository.save(any(UserLike.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ideaRepository.save(any(Idea.class))).thenReturn(idea);
        when(ideaMapper.mapToDto(idea)).thenReturn(TestDataProvider.SPACE_ELEVATOR);

        // when
        IdeaDTO result = ideaService.endorseIdea(ideaId);

        // then
        assertEquals(1, idea.getLikes().size());
        assertEquals(TestDataProvider.SPACE_ELEVATOR, result);
        verify(likeRepository).save(any(UserLike.class));
    }

    @Test
    void testEndorseIdea_InvalidId_ThrowsIdeaNotFoundException() {
        // given
        Long ideaId = 2L;
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.empty());

        // when then
        IdeaNotFoundException thrown = assertThrows(IdeaNotFoundException.class, () -> {
            ideaService.endorseIdea(ideaId);
        });

        assertTrue(thrown.getMessage().contains("Idea with id=" + ideaId + " not found"));
        verify(ideaRepository).findById(ideaId);
    }

    @Test
    void testAddComment_ValidIdeaIdAndInput_AddsAndReturnsCommentDto() {
        // given
        Long ideaId = 3L;
        Idea idea = new Idea();
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));
        idea.setComments(new ArrayList<>());
        Comment comment = new Comment();
        SubmitCommentRequest request = ENTHUSIASTIC_COMMENT_REQUEST;
        when(commentMapper.mapFromCommand(request)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.mapToDto(comment)).thenReturn(TestDataProvider.SUPPORTIVE_COMMENT);

        // when
        CommentDTO result = ideaService.addComment(ideaId, request);

        // then
        assertEquals(TestDataProvider.SUPPORTIVE_COMMENT, result);
        assertEquals(idea, comment.getIdea());
        assertEquals(1, idea.getComments().size());
        verify(commentRepository).save(comment);
    }

    @Test
    void testAddComment_InvalidIdeaId_ThrowsIdeaNotFoundException() {
        // given
        Long ideaId = 3L;
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.empty());

        // when / then
        IdeaNotFoundException thrown = assertThrows(IdeaNotFoundException.class, () -> {
            ideaService.addComment(ideaId, ENTHUSIASTIC_COMMENT_REQUEST);
        });

        assertTrue(thrown.getMessage().contains("Idea with id=" + ideaId + " not found"));
        verify(ideaRepository).findById(ideaId);
    }
}