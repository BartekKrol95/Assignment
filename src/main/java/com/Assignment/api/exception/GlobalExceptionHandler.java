package com.Assignment.api.exception;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationValidationDto> handleMethodArgumentsNotValidException(MethodArgumentNotValidException exception) {
        ValidationValidationDto errorDto = new ValidationValidationDto();
        exception.getFieldErrors().forEach(fieldError ->
                errorDto.addValidationError(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleProjectNotFoundException(ProjectNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDto(exception.getMessage()));
    }

    @ExceptionHandler(IdeaNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleIdeaNotFoundException(IdeaNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDto(exception.getMessage()));
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionDto> handleOptimisticLockingFailureException(OptimisticLockingFailureException exception) {
        String message = "The resource you are trying to update has been modified by another process. Please try again.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDto(message));
    }
}
