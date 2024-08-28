package com.Assignment.domain.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmitCommentRequest {

    @NotNull(message = "NULL_VALUE")
    @Pattern(regexp = "^[A-Z][a-z]{1,18}(?: [A-Z][a-z]{1,30})?$", message = "PATTERN_MISMATCH_{regexp}")
    private String authorName;

    @NotNull(message = "NULL_VALUE")
    @Size(max = 255, message = "DESCRIPTION_TOO_LONG")
    private String content;
}