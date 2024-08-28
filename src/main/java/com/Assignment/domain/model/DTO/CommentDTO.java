package com.Assignment.domain.model.DTO;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class CommentDTO {
    private Long id;
    private String authorName;
    private String content;
    private LocalDateTime createdAt;
}