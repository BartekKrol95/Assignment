package com.Assignment.utils;

import com.Assignment.domain.model.DTO.CommentDTO;
import com.Assignment.domain.model.DTO.IdeaDTO;
import com.Assignment.domain.model.DTO.ProjectDTO;
import com.Assignment.domain.model.request.RegisterIdeaRequest;
import com.Assignment.domain.model.request.RegisterProjectRequest;
import com.Assignment.domain.model.request.SubmitCommentRequest;

import java.time.LocalDateTime;
import java.util.List;

public class TestDataProvider {
    public static final ProjectDTO APOLLO_MISSIONS;
    public static final ProjectDTO CERN_HADRON_COLLIDER;
    public static final List<ProjectDTO> FAMOUS_PROJECT_DTOS;

    public static final RegisterProjectRequest NASA_NEW_MISSION_REQUEST;

    public static final IdeaDTO SPACE_ELEVATOR;
    public static final IdeaDTO QUANTUM_COMPUTING;
    public static final IdeaDTO FUSION_ENERGY;
    public static final List<IdeaDTO> INNOVATIVE_IDEA_DTOS;

    public static final RegisterIdeaRequest ADVANCED_PROPULSION_IDEA_REQUEST;

    public static final CommentDTO SUPPORTIVE_COMMENT;
    public static final CommentDTO INSPIRATIONAL_COMMENT;
    public static final List<CommentDTO> COMMENTS_FOR_IDEA;

    public static final SubmitCommentRequest ENTHUSIASTIC_COMMENT_REQUEST;

    static {
        APOLLO_MISSIONS = new ProjectDTO();
        APOLLO_MISSIONS.setName("Apollo Missions");
        APOLLO_MISSIONS.setDescription("The series of missions aimed at landing humans on the Moon.");

        CERN_HADRON_COLLIDER = new ProjectDTO();
        CERN_HADRON_COLLIDER.setName("CERN Large Hadron Collider");
        CERN_HADRON_COLLIDER.setDescription("The world's largest and most powerful particle collider.");

        FAMOUS_PROJECT_DTOS = List.of(APOLLO_MISSIONS, CERN_HADRON_COLLIDER);

        NASA_NEW_MISSION_REQUEST = new RegisterProjectRequest();
        NASA_NEW_MISSION_REQUEST.setName("Mars Rover 2024");
        NASA_NEW_MISSION_REQUEST.setDescription("A new mission to deploy an advanced rover on Mars in 2024.");

        SPACE_ELEVATOR = new IdeaDTO();
        SPACE_ELEVATOR.setTitle("Space Elevator");
        SPACE_ELEVATOR.setDescription("A proposal for a cable-based transport system connecting Earth to space.");
        SPACE_ELEVATOR.setAuthor("Arthur C. Clarke");

        QUANTUM_COMPUTING = new IdeaDTO();
        QUANTUM_COMPUTING.setTitle("Quantum Computing");
        QUANTUM_COMPUTING.setDescription("An advanced computing paradigm based on quantum mechanics.");
        QUANTUM_COMPUTING.setAuthor("Richard Feynman");

        FUSION_ENERGY = new IdeaDTO();
        FUSION_ENERGY.setTitle("Fusion Energy");
        FUSION_ENERGY.setDescription("Harnessing the power of nuclear fusion to provide limitless clean energy.");
        FUSION_ENERGY.setAuthor("Hans Bethe");

        INNOVATIVE_IDEA_DTOS = List.of(QUANTUM_COMPUTING, FUSION_ENERGY);

        ADVANCED_PROPULSION_IDEA_REQUEST = new RegisterIdeaRequest();
        ADVANCED_PROPULSION_IDEA_REQUEST.setTitle("Advanced Propulsion Systems");
        ADVANCED_PROPULSION_IDEA_REQUEST.setDescription("Developing new propulsion systems for deep space exploration.");
        ADVANCED_PROPULSION_IDEA_REQUEST.setAuthor("Elon Musk");

        SUPPORTIVE_COMMENT = new CommentDTO();
        SUPPORTIVE_COMMENT.setAuthorName("Neil Armstrong");
        SUPPORTIVE_COMMENT.setContent("This idea is revolutionary and could change the future of space travel.");
        SUPPORTIVE_COMMENT.setCreatedAt(LocalDateTime.now());

        INSPIRATIONAL_COMMENT = new CommentDTO();
        INSPIRATIONAL_COMMENT.setAuthorName("Buzz Aldrin");
        INSPIRATIONAL_COMMENT.setContent("A groundbreaking idea that pushes the boundaries of human achievement.");
        INSPIRATIONAL_COMMENT.setCreatedAt(LocalDateTime.now());

        COMMENTS_FOR_IDEA = List.of(SUPPORTIVE_COMMENT, INSPIRATIONAL_COMMENT);

        ENTHUSIASTIC_COMMENT_REQUEST = new SubmitCommentRequest();
        ENTHUSIASTIC_COMMENT_REQUEST.setAuthorName("Carl Sagan");
        ENTHUSIASTIC_COMMENT_REQUEST.setContent("Incredible concept! This could lead us to the stars.");
    }
}