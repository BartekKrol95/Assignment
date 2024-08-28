package com.Assignment.api.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidationValidationDto extends ExceptionDto {

    private final List<ViolationInfo> validationErrors = new ArrayList<>();

    public ValidationValidationDto() {
        super("Validation Errors found");
    }

    public void addValidationError(String field, String message) {
        validationErrors.add(new ViolationInfo(field, message));
    }

    public record ViolationInfo(String field, String message) {
    }
}
