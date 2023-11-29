package com.nobita.microservices.transactionservice.feignclient;

import com.nobita.microservices.transactionservice.models.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "account-service")
public interface AccountProxy {
    @GetMapping("/accounts/get-account-by-account-number")
    Account getAccountByAccNum(@RequestHeader("account-number") String accountNumber);
    @PostMapping("/accounts/add-account")
    Account addAccount(@RequestBody Account account);
}
