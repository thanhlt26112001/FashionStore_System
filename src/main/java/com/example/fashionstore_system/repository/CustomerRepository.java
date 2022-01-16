package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Customer;
import com.example.fashionstore_system.entity.Product;
import com.example.fashionstore_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %?1%")
    public List<Customer> searchCustomerByName(String keyword);
}
