package com.myapi.cars.handler;

import com.myapi.cars.error.ApiError;
import com.myapi.cars.error.ApiValidationError;
import com.myapi.cars.exception.EntityAlreadyExistsException;
import com.myapi.cars.exception.EntityNotFoundException;
import com.myapi.cars.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
    Useless, but I'm keeping it for reference
*/

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException runtimeException) {
        ApiError apiError = new ApiError(HttpStatus.BAD_GATEWAY, runtimeException.getMessage(), runtimeException);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, validationException.getMessage(), validationException);
        apiError.setSubErrors(validationException.getViolations().stream().map(ApiValidationError::new).toList());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityDoesNotExistsException(
            EntityNotFoundException entityNotFoundException) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, entityNotFoundException.getMessage(),
                entityNotFoundException);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistsException(
            EntityAlreadyExistsException entityAlreadyExistsException) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, entityAlreadyExistsException.getMessage(),
                entityAlreadyExistsException);
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
