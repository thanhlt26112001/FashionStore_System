package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    public List<Cart> findByCustomer_Id(int customerId) ;

}
