package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public void saveCustomer(Customer customer) {
        repository.save(customer);
    }

    public Customer findByEmail(String email){
        return repository.findByEmail(email);

    }
}
