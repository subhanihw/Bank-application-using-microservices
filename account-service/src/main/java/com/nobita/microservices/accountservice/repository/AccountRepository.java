package com.nobita.microservices.accountservice.repository;

import com.nobita.microservices.accountservice.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByAccountNumber(String accountNum);

    int countByAccountType(String type);
}
