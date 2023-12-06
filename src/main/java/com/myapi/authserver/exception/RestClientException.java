package com.myapi.authserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class RestClientException extends ServiceException {

    private final HttpStatusCode statusCode;

    private final String responseMessage;

    public RestClientException(String errorMessage, String responseMessage, HttpStatusCode statusCode) {
        super(errorMessage);
        this.responseMessage = responseMessage;
        this.statusCode = statusCode;
    }
}
