package com.nobita.microservices.transactionservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private double amount;
    private double charge;
    private double initialBalance;
    private double remainingBalance;
    private LocalDateTime transactionTime;

    private Long accountId;
}
