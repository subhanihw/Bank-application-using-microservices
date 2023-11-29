package com.nobita.microservices.customerservice.service;

import com.nobita.microservices.customerservice.exceptions.CustomerNotFoundException;
import com.nobita.microservices.customerservice.exceptions.InvalidFieldException;
import com.nobita.microservices.customerservice.model.AddCustomerDTO;
import com.nobita.microservices.customerservice.model.Customer;
import com.nobita.microservices.customerservice.model.IsExistsResponse;
import com.nobita.microservices.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer findByCustomerId(int id) throws CustomerNotFoundException {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty())
            throw new CustomerNotFoundException();
        return customer.get();
    }

    public IsExistsResponse isExist(int id) {
        Optional<Customer> customer = repository.findById(id);
        IsExistsResponse existsResponse = new IsExistsResponse();
        existsResponse.setExist(customer.isPresent());
        return existsResponse;
    }

    public Customer addCustomer(Customer customer) {
        return repository.save(customer);
    }

    public Customer deleteCustomer(int id) throws CustomerNotFoundException {
        Customer customer = findByCustomerId(id);
        repository.deleteById(id);
        return customer;
    }

    public Customer updateCustomer(int id, AddCustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer customer = findByCustomerId(id);
        customer.setName(customerDTO.getName());
        customer.setPanNum(customerDTO.getPanNum());
        return repository.save(customer);
    }

    public Customer updateCustomerByFields(int id, Map<String, Object> fields)
            throws InvalidFieldException, CustomerNotFoundException
    {
        Customer customer = findByCustomerId(id);

        for (Map.Entry<String, Object> entry : fields.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            Field field = ReflectionUtils.findField(Customer.class, key);
            if (field == null) {
                throw new InvalidFieldException("Invalid Field Name");
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, customer, value);
        }
        return repository.save(customer);
    }

}