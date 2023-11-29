package com.nobita.microservices.transactionservice.feignclient;

import com.nobita.microservices.transactionservice.models.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient (name = "customer-service")
public interface CustomerProxy {
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable int id);
}
