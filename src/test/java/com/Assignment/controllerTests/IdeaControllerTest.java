package com.Assignment.controllerTests;

import static com.Assignment.utils.TestDataProvider.COMMENTS_FOR_IDEA;
import static com.Assignment.utils.TestDataProvider.INSPIRATIONAL_COMMENT;
import static com.Assignment.utils.TestDataProvider.SUPPORTIVE_COMMENT;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.request.RegisterIdeaRequest;
import com.Assignment.domain.model.request.SubmitCommentRequest;
import com.Assignment.domain.service.IdeaServiceImplementation;
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
public class IdeaControllerTest {

    @MockBean
    private IdeaServiceImplementation ideaServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFetchAll_ValidPageable_ReturnsPagedIdeas() throws Exception {
        // given
        List<IdeaDTO> ideaDTOs = TestDataProvider.INNOVATIVE_IDEA_DTOS;
        Pageable pageable = PageRequest.of(0, 2);
        Page<IdeaDTO> page = new PageImpl<>(ideaDTOs, pageable, ideaDTOs.size());

        when(ideaServiceImpl.fetchAllIdeas(pageable)).thenReturn(page);

        // when then
        mockMvc.perform(get("/api/v1/projects/ideas?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.number", is(0)))
                .andExpect(jsonPath("$.size", is(2)));

        verify(ideaServiceImpl).fetchAllIdeas(pageable);
    }

    @ParameterizedTest
    @MethodSource("provideCreateIdeaRequests")
    void testRegisterIdea_WithValidAndInvalidInputs(String title, String expectedErrorMessage) throws Exception {
        // given
        RegisterIdeaRequest request = new RegisterIdeaRequest();
        request.setTitle(title);
        request.setDescription(TestDataProvider.ADVANCED_PROPULSION_IDEA_REQUEST.getDescription());
        request.setAuthor(TestDataProvider.ADVANCED_PROPULSION_IDEA_REQUEST.getAuthor());

        if (expectedErrorMessage == null) {
            // Successful case
            IdeaDTO ideaDto = TestDataProvider.FUSION_ENERGY;
            when(ideaServiceImpl.registerIdea(any())).thenReturn(ideaDto);

            // when then
            mockMvc.perform(post("/api/v1/projects/ideas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title", is(title)))
                    .andExpect(jsonPath("$.description", is(ideaDto.getDescription())))
                    .andExpect(jsonPath("$.author", is(ideaDto.getAuthor())));

            verify(ideaServiceImpl).registerIdea(any());
        } else {
            // Failure case
            mockMvc.perform(post("/api/v1/projects/ideas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors[0].message").value(expectedErrorMessage));
        }
    }

    @Test
    void testEndorseIdea_ValidId_IncrementsLikesAndReturnsIdea() throws Exception {
        // given
        Long ideaId = 1L;
        IdeaDTO ideaDto = TestDataProvider.QUANTUM_COMPUTING;
        ideaDto.setLikesCount(1);
        when(ideaServiceImpl.endorseIdea(ideaId)).thenReturn(ideaDto);

        // when then
        mockMvc.perform(post("/api/v1/projects/ideas/{ideaId}/like", ideaId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.likesCount").value(1));

        verify(ideaServiceImpl).endorseIdea(ideaId);
    }

    @Test
    void testSubmitComment_ValidIdeaIdAndInput_AddsAndReturnsCommentDto() throws Exception {
        // given
        Long ideaId = 1L;
        CommentDTO commentDto = SUPPORTIVE_COMMENT;
        SubmitCommentRequest commentRequest = TestDataProvider.ENTHUSIASTIC_COMMENT_REQUEST;

        when(ideaServiceImpl.addComment(anyLong(), any())).thenReturn(commentDto);

        // when  then
        mockMvc.perform(post("/api/v1/projects/ideas/{ideaId}/comments", ideaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.content", is(commentDto.getContent())));

        verify(ideaServiceImpl).addComment(anyLong(), any());
    }

    @Test
    void testFetchCommentsByIdeaId_ValidIdeaId_ReturnsComments() throws Exception {
        // given
        Long ideaId = 1L;

        when(ideaServiceImpl.fetchCommentsByIdeaId(ideaId)).thenReturn(COMMENTS_FOR_IDEA);

        // when then
        mockMvc.perform(get("/api/v1/projects/ideas/{ideaId}/comments", ideaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorName", is(SUPPORTIVE_COMMENT.getAuthorName())))
                .andExpect(jsonPath("$[0].content", is(SUPPORTIVE_COMMENT.getContent())))
                .andExpect(jsonPath("$[1].authorName", is(INSPIRATIONAL_COMMENT.getAuthorName())))
                .andExpect(jsonPath("$[1].content", is(INSPIRATIONAL_COMMENT.getContent())));

        verify(ideaServiceImpl).fetchCommentsByIdeaId(ideaId);
    }

    static Stream<Arguments> provideCreateIdeaRequests() {
        return Stream.of(
                arguments("Fusion Energy", null),
                arguments("invalidTitle123", "PATTERN_MISMATCH_^[A-Z][a-z]{1,18}(?: [A-Z][a-z]{1,30})?$")
        );
    }
}