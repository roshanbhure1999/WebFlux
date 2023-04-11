package com.WebFlux.WebFlux.exception;

import org.springframework.http.HttpStatus;

public class UserException  extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final HttpStatus httpStatus ;

    public UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}