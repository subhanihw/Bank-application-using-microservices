package com.nobita.microservices.transactionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
