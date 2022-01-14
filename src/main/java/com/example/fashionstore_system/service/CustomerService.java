package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer findByEmail(String email){
        return customerRepository.findByEmail(email);

    }
    public Customer getById(int id){
        return customerRepository.getById(id);

    }
}
