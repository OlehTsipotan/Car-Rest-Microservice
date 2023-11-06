package com.myapi.cars.exception;

public class EntityDoesNotExistsException extends ServiceException {
    public EntityDoesNotExistsException(String errorMessage) {
        super(errorMessage);
    }

    public EntityDoesNotExistsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public EntityDoesNotExistsException(Exception e) {
        super(e);
    }
}
