package com.nobita.microservices.customerservice.controller;

import com.nobita.microservices.customerservice.exceptions.CustomerNotFoundException;
import com.nobita.microservices.customerservice.exceptions.InvalidFieldException;
import com.nobita.microservices.customerservice.model.AddCustomerDTO;
import com.nobita.microservices.customerservice.model.Customer;
import com.nobita.microservices.customerservice.model.IsExistsResponse;
import com.nobita.microservices.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CustomerController {
    private final CustomerService service;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) throws CustomerNotFoundException {
        Customer customer = service.findByCustomerId(id);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody AddCustomerDTO customer) {
        Customer customer1 = modelMapper.map(customer, Customer.class);
        Customer addedCustomer = service.addCustomer(customer1);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCustomer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> editCustomer
            (@PathVariable int id, @Valid @RequestBody AddCustomerDTO customer) throws CustomerNotFoundException
    {
        Customer newCustomer = service.updateCustomer(id, customer);
        return ResponseEntity.ok(newCustomer);
    }

    @GetMapping("/customers/is-exists/{id}")
    public ResponseEntity<IsExistsResponse> isExists(@PathVariable int id) {
        return ResponseEntity.ok(service.isExist(id));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Customer> deleteCustomer
            (@PathVariable int id) throws CustomerNotFoundException
    {
        Customer customer = service.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Customer> editCustomerFields(@PathVariable int id, @RequestBody Map<String, Object> fields)
            throws CustomerNotFoundException, InvalidFieldException
    {
        return ResponseEntity.ok(service.updateCustomerByFields(id, fields));
    }
}
