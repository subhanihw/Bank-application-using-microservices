package com.nobita.microservices.transactionservice.services;


import com.nobita.microservices.transactionservice.exceptions.*;
import com.nobita.microservices.transactionservice.feignclient.AccountProxy;
import com.nobita.microservices.transactionservice.feignclient.CustomerProxy;
import com.nobita.microservices.transactionservice.models.*;
import com.nobita.microservices.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.nobita.microservices.transactionservice.models.Constants.TRANSACTION_CHARGE;


@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final AccountProxy accountProxy;
    private final CustomerProxy customerProxy;

    public TransactionService(TransactionRepository repository,
                              AccountProxy accountProxy, CustomerProxy customerProxy)
    {
        this.repository = repository;
        this.accountProxy = accountProxy;
        this.customerProxy = customerProxy;
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAllByOrderByTransactionTimeDesc();
    }

    public Transaction addTransaction(AddTransactionDTO transactionDTO)
            throws AccountNotFoundException, InvalidTransactionType,
            RequiresPanException, MinBalanceException, InsufficientBalanceException
    {
        Account account = accountProxy.getAccountByAccNum(transactionDTO.getAccountNum());

        if (account == null)
            throw new AccountNotFoundException();
        String transactionTypeStr = transactionDTO.getType();
        TransactionType transactionType;
        try {
            transactionType = TransactionType.valueOf(transactionTypeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidTransactionType();
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionType);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setCharge(TRANSACTION_CHARGE);
        transaction.setInitialBalance(account.getBalance());
        transaction.setAccountId(account.getAccountId());

        switch (transactionType) {
            case DEPOSIT:
                deposit(account, transactionDTO.getAmount());
                break;
            case WITHDRAW:
                withdraw(account, transactionDTO.getAmount());
                break;
            default:
                throw new InvalidTransactionType();
        }
        transaction.setRemainingBalance(account.getBalance());
        accountProxy.addAccount(account);
        return repository.save(transaction);
    }

    private void deposit(Account account, double amount) throws MinBalanceException, RequiresPanException
    {
        Customer customer = customerProxy.getCustomerById(Math.toIntExact(account.getCustomerId()));
        if (amount <= 0)
            throw new MinBalanceException("Amount must be greater than 0.");
        if (amount > 5000000 && (customer.getPanNum() == null || customer.getPanNum().isEmpty())) {
            throw new RequiresPanException("Requires Pan details to deposit more than 5000000");
        }
        if (amount < 100) {
            throw new MinBalanceException("Amount must be greater than 100 to deposit");
        }
        account.setBalance(account.getBalance() + amount);
        account.setBalance(account.getBalance() - TRANSACTION_CHARGE);
    }

    private void withdraw(Account account, double amount)
            throws InsufficientBalanceException, MinBalanceException
    {
        double balance = account.getBalance();
        if (balance - amount < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        if (balance - amount < 110) {
            throw new MinBalanceException("Balance must be greater than 100 after withdraw. Transaction Charge: $10");
        }
        account.setBalance(account.getBalance() - amount);
        account.setBalance(account.getBalance() - TRANSACTION_CHARGE);
    }
    public List<Transaction> getTransactionByAccountNum(String accountNum)  {
        Account account = accountProxy.getAccountByAccNum(accountNum);
        return repository.findAllByAccountIdOrderByTransactionTimeDesc(account.getAccountId());
    }
}
