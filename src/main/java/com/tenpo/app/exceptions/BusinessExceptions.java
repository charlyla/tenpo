package com.tenpo.app.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessExceptions extends RuntimeException {

    private HttpStatus status;

    public BusinessExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}