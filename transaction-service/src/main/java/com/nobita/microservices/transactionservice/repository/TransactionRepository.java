package com.nobita.microservices.transactionservice.repository;

import com.nobita.microservices.transactionservice.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionTimeDesc();

    List<Transaction> findAllByAccountIdOrderByTransactionTimeDesc(Long accountId);
}
