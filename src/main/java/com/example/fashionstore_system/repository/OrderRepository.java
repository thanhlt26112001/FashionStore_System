package com.example.fashionstore_system.repository;


import com.example.fashionstore_system.entity.Category;
import com.example.fashionstore_system.entity.Order;
import com.example.fashionstore_system.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order>findAllByCustomerId(Integer customerId);

    public Page<Order> findByReceiverNameContaining(String keyword, Pageable pageable);

    @Query("SELECT s FROM Order s WHERE CONCAT(s.receiverName, ' ') LIKE %?1%")
    public List<Order>  search(String keyword);

}
