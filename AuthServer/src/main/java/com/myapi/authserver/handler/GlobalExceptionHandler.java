package com.myapi.authserver.handler;

import com.myapi.authserver.exception.RestClientException;
import com.myapi.authserver.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        log.error("RuntimeException: {}", e.getMessage());
        return ErrorResponse.builder(e, HttpStatus.BAD_GATEWAY, e.getMessage()).title("Runtime Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler
    public ErrorResponse handleServiceException(ServiceException e) {
        log.error("ServiceException: {}", e.getMessage());
        return ErrorResponse.builder(e, HttpStatus.BAD_GATEWAY, e.getMessage()).title("Service Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage()).title("IllegalArgument Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(HttpClientErrorException.class)
    public ErrorResponse handleHttpClientErrorException(HttpClientErrorException e) {
        log.error("HttpClientErrorException: {}", e.getMessage());
        return ErrorResponse.builder(e, e.getStatusCode(), e.getMessage()).title("HttpClientError Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(RestClientException.class)
    public ErrorResponse handleRestClientException(RestClientException e) {
        log.error("RestClientException: {}", e.getMessage());
        return ErrorResponse.builder(e, e.getStatusCode(), e.getMessage()).title("RestClient Exception")
                .property("response", e.getResponseMessage())
                .property("timestamp", Instant.now()).build();
    }
}
