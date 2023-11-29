package com.nobita.microservices.accountservice.feignClient;

import com.nobita.microservices.accountservice.models.IsExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "http://localhost:9000")
public interface CustomerProxy {
    @GetMapping("/customers/is-exists/{id}")
    IsExistsResponse isExists(@PathVariable int id);
}
