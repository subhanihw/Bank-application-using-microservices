package com.nobita.microservices.accountservice.service;

import com.nobita.microservices.accountservice.exceptions.AccountNotFoundException;
import com.nobita.microservices.accountservice.exceptions.CustomerNotFoundException;
import com.nobita.microservices.accountservice.exceptions.InvalidAccountTypeException;
import com.nobita.microservices.accountservice.feignClient.CustomerProxy;
import com.nobita.microservices.accountservice.models.Account;
import com.nobita.microservices.accountservice.models.IsExistsResponse;
import com.nobita.microservices.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    private final AccountRepository repository;
    private final CustomerProxy customerProxy;


    public AccountService(AccountRepository repository, CustomerProxy customerProxy) {
        this.repository = repository;
        this.customerProxy = customerProxy;
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public Account addAccount(Account account) throws InvalidAccountTypeException, CustomerNotFoundException {
        IsExistsResponse existsResponse = customerProxy.isExists(Math.toIntExact(account.getCustomerId()));

        if (!existsResponse.isExist()) {
            throw new CustomerNotFoundException();
        }
        String accountType = account.getAccountType();
        int count = getCountOfAccountType(accountType);

        String prefix;
        if (accountType.equalsIgnoreCase("Current")) {
            prefix = "C";
        } else if (accountType.equalsIgnoreCase("Savings")) {
            prefix = "S";
        }else  if (accountType.equalsIgnoreCase("Fixed Deposit")){
            prefix = "FD";
        }else {
            throw new InvalidAccountTypeException();
        }

        String accountNum = String.format("%s%d", prefix, count+1);
        account.setAccountNumber(accountNum);
        return repository.save(account);
    }

    public Account getAccountByAccountNumber(String accountNum) throws AccountNotFoundException {
        Optional<Account> accountByAccountNumber = repository.findAccountByAccountNumber(accountNum);
        if(accountByAccountNumber.isEmpty())
            throw new AccountNotFoundException();
        return accountByAccountNumber.get();
    }

    public void updateBalance(Account account, double balance) {
        account.setBalance(balance);
        repository.save(account);
    }

    public void deleteAccount(Account account) {
        repository.delete(account);
    }

    public int getCountOfAccountType(String type) {
        return repository.countByAccountType(type);
    }

//    public List<Account> getAllAccountsByCustomerId(Customer customer) {
//        return repository.getAccountsByCustomer(customer);
//    }

    public boolean isExistsAccountType(List<Account> accountList, String accountType) {
        return accountList.stream().anyMatch(account -> account.getAccountType().equals(accountType));
    }

    public Account saveAccount(Account account) {
        return repository.save(account);
    }
}
