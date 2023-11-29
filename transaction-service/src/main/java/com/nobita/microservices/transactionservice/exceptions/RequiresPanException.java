package com.nobita.microservices.transactionservice.exceptions;

public class RequiresPanException extends Exception{
    public RequiresPanException() {
    }

    public RequiresPanException(String msg) {
        super(msg);
    }
}
