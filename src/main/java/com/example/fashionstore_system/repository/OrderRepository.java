package com.example.fashionstore_system.repository;

import com.example.fashionstore_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order>findAllByCustomerId(Integer customerId);
}
