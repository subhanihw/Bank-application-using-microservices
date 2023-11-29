package com.nobita.microservices.transactionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AccountTypeAlreadyExistsException extends Exception {
    public AccountTypeAlreadyExistsException() {
    }

    public AccountTypeAlreadyExistsException(String msg) {
        super(msg);
    }
}
