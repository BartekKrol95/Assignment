package com.Assignment.domain.model.DTO;

import lombok.Data;

@Data
public class IdeaDTO {
    private Long id;
    private String title;
    private String description;
    private String author;
    private String graphicDesignerName;
    private String imageUrl;
    private int likesCount;
}