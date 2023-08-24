package com.tenpo.app.controller;

import com.tenpo.app.dto.ErrorDTO;
import com.tenpo.app.exceptions.BusinessExceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = BusinessExceptions.class)
    public ResponseEntity<ErrorDTO> businessExceptionHandler (BusinessExceptions ex) {
        ErrorDTO error = ErrorDTO.builder().msg(ex.getMessage()).build();
        return new ResponseEntity<>(error, ex.getStatus());
    }
}
