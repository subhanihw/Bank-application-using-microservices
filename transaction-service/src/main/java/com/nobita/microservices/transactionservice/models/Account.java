package com.nobita.microservices.transactionservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long accountId;
    private String accountNumber;
    private double balance;
    private String accountType;
    private Double interestRate;
    private Long customerId;
}
