package com.Assignment.api.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionDto {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String message;
}