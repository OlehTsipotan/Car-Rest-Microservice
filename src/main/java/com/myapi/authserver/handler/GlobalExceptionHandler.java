package com.myapi.authserver.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_GATEWAY, e.getMessage()).title("Runtime Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) {
        return ErrorResponse.builder(e, e.getStatusCode(), e.getMessage()).title("HttpClientError Exception")
                .property("timestamp", Instant.now()).build();
    }
}
