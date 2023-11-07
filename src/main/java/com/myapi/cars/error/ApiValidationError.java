package com.myapi.cars.error;

import com.myapi.cars.exception.FieldViolation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(FieldViolation fieldViolation) {
        this.object = fieldViolation.getObjectName();
        this.field = fieldViolation.getField();
        this.rejectedValue = fieldViolation.getRejectedValue();
        this.message = fieldViolation.getMessage();
    }
}