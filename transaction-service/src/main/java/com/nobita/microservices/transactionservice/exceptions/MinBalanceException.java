package com.nobita.microservices.transactionservice.exceptions;

public class MinBalanceException extends Exception{
    public MinBalanceException() {
    }

    public MinBalanceException(String msg) {
        super(msg);
    }
}
