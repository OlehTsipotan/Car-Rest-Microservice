package com.myapi.cars.handler;

import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
import com.myapi.cars.exception.ServiceException;
import com.myapi.cars.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse handleRuntimeException(RuntimeException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_GATEWAY, e.getMessage()).title("Runtime Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ExceptionHandler(ServiceException.class)
    public ErrorResponse handleServiceException(ServiceException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_GATEWAY, e.getMessage()).title("Service Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException e) {
        return ErrorResponse.builder(e, HttpStatus.CONFLICT, e.getMessage()).title("Validation Exception")
                .property("violations", e.getViolations()).property("timestamp", Instant.now()).build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage()).title("EntityNotFound Exception")
                .property("timestamp", Instant.now()).build();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ErrorResponse handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return ErrorResponse.builder(e, HttpStatus.CONFLICT, e.getMessage()).title("EntityAlreadyExists Exception")
                .property("timestamp", Instant.now()).build();
    }
}
