package com.example.fashionstore_system.service;

import com.example.fashionstore_system.entity.User;
import com.example.fashionstore_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User getCustomerByUserName(String username) {
        return repository.findByUsername(username);
    }
}
