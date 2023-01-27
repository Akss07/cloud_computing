package com.neu.cloudapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedError extends RuntimeException{
    public UnauthorizedError(String message) {
        super(message);
    }
}
