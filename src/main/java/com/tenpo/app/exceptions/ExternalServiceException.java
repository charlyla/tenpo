package com.tenpo.app.exceptions;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends RuntimeException {

    private HttpStatus status;

    public ExternalServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
