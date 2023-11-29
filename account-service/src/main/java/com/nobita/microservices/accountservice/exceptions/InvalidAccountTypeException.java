package com.nobita.microservices.accountservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidAccountTypeException extends Exception{
    public InvalidAccountTypeException() {
    }

    public InvalidAccountTypeException(String msg) {
        super(msg);
    }
}
