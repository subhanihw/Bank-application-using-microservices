package com.nobita.microservices.accountservice.controller;

import com.nobita.microservices.accountservice.exceptions.AccountNotFoundException;
import com.nobita.microservices.accountservice.exceptions.CustomerNotFoundException;
import com.nobita.microservices.accountservice.exceptions.InvalidAccountTypeException;
import com.nobita.microservices.accountservice.models.Account;
import com.nobita.microservices.accountservice.models.CreateAccountDTO;
import com.nobita.microservices.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccountController {
    private Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public AccountController(AccountService accountService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        logger.info("Get All Account is Called");
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/accounts/get-account-by-account-number")
    public ResponseEntity<Account> getAccountByAccNum(@RequestHeader("account-number") String accountNumber) throws AccountNotFoundException {
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts/add-account")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.saveAccount(account));
    }
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO accountDTO) throws InvalidAccountTypeException, CustomerNotFoundException {
        Account account = modelMapper.map(accountDTO, Account.class);
        return ResponseEntity.ok(accountService.addAccount(account));
    }

    @PutMapping("/accounts/update-balance")
    public ResponseEntity<Account> updateBalance
            (@RequestHeader("account-number") String accountNum, @RequestHeader("balance") double balance) throws AccountNotFoundException {
        Account account = accountService.getAccountByAccountNumber(accountNum);
        accountService.updateBalance(account, balance);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/accounts/delete-account")
    public ResponseEntity<Account> deleteAccount
            (@RequestHeader("account-number") String accountNum) throws AccountNotFoundException
    {
        Account account = accountService.getAccountByAccountNumber(accountNum);
        accountService.deleteAccount(account);
        return ResponseEntity.ok(account);
    }
}
